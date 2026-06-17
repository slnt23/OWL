# sugarcane 模块 Redis 缓存优化方案

## 缓存穿透问题

**缓存穿透**：查询一个**数据库中不存在**的数据（如 itemId=99999 的商品），缓存中自然也没有，每次请求都会穿透到 DB 执行昂贵的 5 表 JOIN 查询。恶意攻击者可以遍历大量不存在的 ID，击垮数据库。

sugarcane 的风险点：
- `POST /price/latest` → 传入不存在的 itemId，每次都执行大量 JOIN
- `POST /price/trend` → 不存在的 itemId 组合
- `GET /category/tree` → 没啥风险（全查，不存在按 ID 查的场景）

以下两种方案，可以组合使用。

---

## 方案一：缓存空对象

**原理**：当 DB 查询返回 null 时，缓存一个**占位空对象**并设较短 TTL。下次请求同一不存在 ID，直接走缓存返回空，不再打 DB。

### Spring Cache 方式

Spring Cache 默认用 `disableCachingNullValues()` 禁止缓存 null。改为：**不禁止 null，缩短 null 的缓存时间**。

**步骤 1**：修改 `RedisConfig.java` 的 `cacheManager` — 去掉 `disableCachingNullValues()`，对空值用短 TTL：

```java
@Bean
public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
    ObjectMapper om = new ObjectMapper();
    om.registerModule(new JavaTimeModule());
    om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(om);

    RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(10))
            .serializeKeysWith(
                    RedisSerializationContext.SerializationPair
                            .fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(
                    RedisSerializationContext.SerializationPair
                            .fromSerializer(serializer));
            // 注意：去掉了 .disableCachingNullValues()

    return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(defaultConfig)
            .withCacheConfiguration("priceLatest",
                    defaultConfig.entryTtl(Duration.ofMinutes(1)))
            .withCacheConfiguration("priceTrend",
                    defaultConfig.entryTtl(Duration.ofMinutes(5)))
            .build();
}
```

**步骤 2**：`RecordServiceImpl` 中，`@Cacheable` 不加 `unless`，让 null 也被缓存：

```java
@Override
@Cacheable(value = CacheConstant.PRICE_LATEST, key = "#dto.itemId")
public PriceLatestVO queryLatest(PriceLatestQueryDTO dto) {
    PriceLatestVO vo = recordMapper.selectLatest(dto.getItemId());
    return vo; // null 也缓存，避免穿透
}
```

**缺点**：如果恶意攻击者用大量不同的不存在 ID，Redis 会被无效 key 填满。短 TTL（1 分钟）只能缓解，不能根治。

---

**手动缓存方式**（更精细控制，不依赖 Spring Cache）：

```java
@Service
@RequiredArgsConstructor
public class RecordServiceImpl extends ServiceImpl<RecordMapper, RecordDO> implements RecordService {

    private final RecordMapper recordMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    /** 空对象占位符 */
    private static final PriceLatestVO EMPTY = new PriceLatestVO();
    /** 空对象缓存 TTL：30 秒 */
    private static final int EMPTY_TTL_SECONDS = 30;
    /** 正常缓存 TTL：60 秒 */
    private static final int NORMAL_TTL_SECONDS = 60;

    @Override
    public PriceLatestVO queryLatest(PriceLatestQueryDTO dto) {
        String key = "price:latest:" + dto.getItemId();

        // 1. 查 Redis
        Object cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            if (cached == EMPTY) {               // 空对象标记
                return null;
            }
            return (PriceLatestVO) cached;
        }

        // 2. 查 DB
        PriceLatestVO vo = recordMapper.selectLatest(dto.getItemId());

        // 3. 写 Redis（区分空结果和正常结果）
        if (vo == null) {
            redisTemplate.opsForValue().set(key, EMPTY, EMPTY_TTL_SECONDS, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(key, vo, NORMAL_TTL_SECONDS, TimeUnit.SECONDS);
        }
        return vo;
    }
}
```

> 问题：`== EMPTY` 引用比较在反序列化后不成立（GenericJackson2JsonRedisSerializer 反序列化会创建新对象）。解决：用一个特殊字段标记，如 `PriceLatestVO.EMPTY.setItemName("__NULL__")`，反序列化后判断该字段。

---

## 方案二：布隆过滤器

### 设计原则：通用基础设施，放 common 而非 sugarcane

布隆过滤器是跨模块复用的基础设施（sugarcane 防价格穿透、pitaya 防商品穿透、user 防用户查询穿透），应放在 `common/.../infrastructure/bloom/`，作为通用组件。各业务模块只需在启动时注入自己的数据。

**目录结构**：

```
common/src/main/java/.../infrastructure/bloom/
├── BloomFilterService.java        ← 接口：mightContain(key), add(key)
└── GuavaBloomFilterService.java   ← Guava 内存版实现
```

### 2a. common 层：通用接口 + Guava 实现

`common/pom.xml` 加入 Guava：

```xml
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>33.0.0-jre</version>
</dependency>
```

**接口** — `common/.../infrastructure/bloom/BloomFilterService.java`：

```java
package xyz.nanian.owl.infrastructure.bloom;

/**
 * 布隆过滤器通用接口 — 防缓存穿透
 * 各业务模块注入此接口，向各自的命名空间写入 key
 */
public interface BloomFilterService {

    /** 判断 key 是否可能存在（假阳性率 1%） */
    boolean mightContain(String key);

    /** 新增 key（写入/更新时调用） */
    void add(String key);

    /** 预估元素数量 */
    long approximateCount();
}
```

**Guava 实现** — `common/.../infrastructure/bloom/GuavaBloomFilterService.java`：

```java
package xyz.nanian.owl.infrastructure.bloom;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * 基于 Guava 的内存布隆过滤器
 * 优点：零网络开销、纳秒级判断  |  缺点：重启需重建、多实例各自维护
 *
 * 预估 100 万条 key，假阳性率 1%，内存 ≈ 1.2 MB
 */
@Service
public class GuavaBloomFilterService implements BloomFilterService {

    private BloomFilter<String> filter;

    private static final int EXPECTED_INSERTIONS = 1_000_000;
    private static final double FPP = 0.01;

    @PostConstruct
    public void init() {
        filter = BloomFilter.create(
                Funnels.stringFunnel(StandardCharsets.UTF_8),
                EXPECTED_INSERTIONS,
                FPP);
    }

    @Override
    public boolean mightContain(String key) {
        return filter.mightContain(key);
    }

    @Override
    public void add(String key) {
        filter.put(key);
    }

    @Override
    public long approximateCount() {
        return filter.approximateElementCount();
    }
}
```

> 如需分布式版：引入 Redisson，另写一个 `RedisBloomFilterService implements BloomFilterService`，其他代码零改动。

### 2b. 业务模块：初始化 + 使用

sugarcane 启动时注册自己的数据 — `sugarcane/.../component/SugarcaneBloomInitializer.java`：

```java
package xyz.nanian.owl.sugarcane.component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import xyz.nanian.owl.infrastructure.bloom.BloomFilterService;
import xyz.nanian.owl.sugarcane.domain.entity.ItemDO;
import xyz.nanian.owl.sugarcane.service.ItemService;

import java.util.List;

/**
 * sugarcane 模块布隆过滤器初始化
 * 启动时将所有 itemId 注册进去
 */
@Component
@RequiredArgsConstructor
public class SugarcaneBloomInitializer {

    private final BloomFilterService bloomFilter;
    private final ItemService itemService;

    @PostConstruct
    public void init() {
        List<ItemDO> items = itemService.list();
        for (ItemDO item : items) {
            bloomFilter.add("item:" + item.getId());
        }
    }
}
```

Service 中使用 — `RecordServiceImpl.java`：

```java
@Service
@RequiredArgsConstructor
public class RecordServiceImpl extends ServiceImpl<RecordMapper, RecordDO> implements RecordService {

    private final RecordMapper recordMapper;
    private final BloomFilterService bloomFilter;  // ← 注入通用接口
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public PriceLatestVO queryLatest(PriceLatestQueryDTO dto) {
        String bloomKey = "item:" + dto.getItemId();

        // 0. 布隆过滤器拦截 — 不存在则直接返回
        if (!bloomFilter.mightContain(bloomKey)) {
            return null;
        }

        // 1. 查 Redis
        // 2. 查 DB
        // 3. 写 Redis
    }
}
```

pitaya 同理 — `pitaya/.../component/PitayaBloomInitializer.java`：

```java
@Component
@RequiredArgsConstructor
public class PitayaBloomInitializer {

    private final BloomFilterService bloomFilter;
    private final ProductService productService;

    @PostConstruct
    public void init() {
        productService.list().forEach(p -> bloomFilter.add("product:" + p.getId()));
    }
}
```

**内存估算**：100 万条 key × 1% 假阳性率 ≈ **1.2 MB**，非常小。

---

## 方案对比

| 维度 | 缓存空对象 | Guava 布隆过滤器 (common) | Redisson 布隆过滤器 |
|------|-----------|-----------------|-------------------|
| 实现位置 | 各业务 Service | `common/.../bloom/` | `common/.../bloom/` |
| 实现复杂度 | 低 | 中（接口+实现+各模块初始化） | 中 |
| 额外依赖 | 无 | guava（加到 common） | redisson-spring-boot-starter |
| 空查询内存消耗 | 每个不存在的 ID 占一个 Redis key | JVM ~1.2MB (100万key) | Redis ~10MB |
| 判断速度 | 查 Redis（网络 IO） | 内存 hash（纳秒） | 查 Redis（网络 IO） |
| 能否被"海量随机 ID"打穿 | **能**（Redis 被打满） | 不能 | 不能 |
| 多实例 | 天然共享 | 各自维护，需对齐 | 天然共享 |
| 可复用性 | 侵入各模块 | **全项目复用，各模块只需初始化** | 同左 |
| 假阳性 | 无（DB 真实结果） | ~1% | ~1% |

## 代码分布总览

```
common/infrastructure/bloom/
├── BloomFilterService.java          ← 通用接口
├── GuavaBloomFilterService.java     ← Guava 实现（内存版）
└── RedisBloomFilterService.java     ← Redisson 实现（Redis版，可选）

sugarcane/component/
└── SugarcaneBloomInitializer.java   ← 启动时注册 "item:" + itemId

pitaya/component/
└── PitayaBloomInitializer.java      ← 启动时注册 "product:" + productId

user/component/
└── UserBloomInitializer.java        ← 启动时注册 "user:" + userId（如有需要）
```

## 推荐方案

**组合使用**：**common 泛型布隆过滤器 + 缓存空对象**，三层防御：

```
请求 itemId=99999
  → BloomFilterService.mightContain("item:99999")
      → "不存在" → 直接返回 null（不查 Redis，不查 DB，纳秒级）
      → "可能存在" → 查 Redis → 空对象标记 → 返回 null
                               → 有数据 → 返回
                               → 无数据 → 查 DB → 写 Redis
```

- **布隆过滤器**（common 通用）：挡住 99% 恶意穿透请求
- **缓存空对象**（各 Service 自管）：挡住布隆过滤器假阳性的 1%
- 两者叠加，DB 几乎不被不存在的 ID 打到

**实施顺序**：
1. common 加 Guava 依赖 + 写 `BloomFilterService` 接口 + `GuavaBloomFilterService` 实现
2. sugarcane 加 `SugarcaneBloomInitializer`，RecordService 注入 `BloomFilterService`
3. 后续 pitaya、user 有需要时，加自己的 Initializer 即可复
