# sugarcane 缓存雪崩解决方案

## 什么是缓存雪崩

两种情况：

- **场景 A**：大量缓存 key **同时过期**（如同一批 TTL 到期），瞬间所有请求打到 DB
- **场景 B**：**Redis 宕机**或重启，所有请求直接穿透到 DB

与击穿的区别：击穿是**单个热点 key** 过期，雪崩是**大量 key** 同时出问题。

## sugarcane 风险点

| 场景 | 触发条件 |
|------|---------|
| 批量导入价格数据 | 同一批写入的 key 设了相同的 TTL，到期同时失效 |
| 定时任务全量刷新缓存 | 一次性 invalidate 大量 key |
| Redis Sentinel 主从切换 | 短暂不可用，所有请求瞬间打 DB |
| 重启时缓存为空 | 启动后冷缓存，流量涌入 DB |

---

## 方案一：TTL 加随机偏移

**原理**：给每个 key 的 TTL 加上随机值，把同一批过期的 key 打散到不同时间点。

### 工具类

```java
package xyz.nanian.owl.sugarcane.component;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * TTL 随机化工具 — 防止大量 key 同时过期引发雪崩
 *
 * 用法：
 *   int ttl = TtlRandomizer.jitterMinutes(30, 10);  // 30~40 分钟随机
 *   redisTemplate.opsForValue().set(key, data, ttl, TimeUnit.SECONDS);
 */
public final class TtlRandomizer {

    private TtlRandomizer() {}

    /**
     * @param baseSeconds  基础 TTL 秒数
     * @param maxJitterSeconds  最大随机附加秒数
     * @return base + [0, maxJitter] 内的随机值
     */
    public static int jitter(int baseSeconds, int maxJitterSeconds) {
        int jitter = ThreadLocalRandom.current().nextInt(maxJitterSeconds + 1);
        return baseSeconds + jitter;
    }

    /** 分钟版本 */
    public static int jitterMinutes(int baseMinutes, int maxJitterMinutes) {
        return jitter((int) TimeUnit.MINUTES.toSeconds(baseMinutes),
                      (int) TimeUnit.MINUTES.toSeconds(maxJitterMinutes));
    }
}
```

### 效果对比

```
没有随机偏移:
  key1 ──── 30min ──── 过期 ┐
  key2 ──── 30min ──── 过期 ├── 同一秒全部过期，同时打 DB
  key3 ──── 30min ──── 过期 ┘

加了随机偏移 (base=30min, maxJitter=10min):
  key1 ──── 34min ──── 过期
  key2 ──── 38min ──── 过期  ← 分散在 10 分钟内，DB 压力平滑
  key3 ──── 31min ──── 过期
```

### 使用

```java
// 基础 30 分钟，实际 30~40 分钟之间随机过期
int ttl = TtlRandomizer.jitterMinutes(30, 10);
redisTemplate.opsForValue().set(key, value, ttl, TimeUnit.SECONDS);
```

> **Spring Cache 的限制**：`RedisCacheConfiguration.entryTtl()` 是全局固定值，不支持 per-key 随机 TTL。用 `@Cacheable` 时无法享受随机偏移。解决：热点场景走手动缓存 + TtlRandomizer，参考数据用 Spring Cache 设差异化基础 TTL 也能降低同时过期概率。

---

## 方案二：多级缓存（Caffeine L1 + Redis L2）

**原理**：本地 Caffeine 缓存作为一级，Redis 作为二级。Redis 短暂宕机时，Caffeine 还能扛住请求，为 Redis 恢复争取时间。

### 依赖

```xml
<!-- watermelon/sugarcane/pom.xml -->
<dependency>
    <groupId>com.github.ben-manes.caffeine</groupId>
    <artifactId>caffeine</artifactId>
</dependency>
```

### 配置

```java
package xyz.nanian.owl.sugarcane.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 多级缓存：Caffeine (L1 本地) + Redis (L2 远程)
 *
 * 命中顺序：Caffeine → Redis → DB
 * Redis 宕机时，Caffeine 作为最后一道防线
 */
@Configuration
public class MultiLevelCacheConfig {

    @Bean
    @Primary
    public CacheManager compositeCacheManager(RedisCacheManager redisCacheManager) {

        // 分类树 — 全站热点，本地缓存 5 分钟
        CaffeineCache categoryCache = new CaffeineCache("categoryTree",
                Caffeine.newBuilder()
                        .maximumSize(100)
                        .expireAfterWrite(5, TimeUnit.MINUTES)
                        .recordStats()
                        .build());

        // 来源列表 — 变化极少
        CaffeineCache sourceCache = new CaffeineCache("sourceList",
                Caffeine.newBuilder()
                        .maximumSize(50)
                        .expireAfterWrite(10, TimeUnit.MINUTES)
                        .build());

        // 地理数据 — 基本不变
        CaffeineCache geoCache = new CaffeineCache("geoTree",
                Caffeine.newBuilder()
                        .maximumSize(200)
                        .expireAfterWrite(30, TimeUnit.MINUTES)
                        .build());

        CaffeineCache geoChildrenCache = new CaffeineCache("geoChildren",
                Caffeine.newBuilder()
                        .maximumSize(500)
                        .expireAfterWrite(30, TimeUnit.MINUTES)
                        .build());

        CompositeCacheManager composite = new CompositeCacheManager();
        composite.setCacheManagers(
                List.of(categoryCache, sourceCache, geoCache, geoChildrenCache, redisCacheManager));
        composite.setFallbackToNoOpCache(false);
        return composite;
    }
}
```

### 请求链路

```
请求
  └→ Caffeine L1 (本地, 微秒级)
       ├─ hit → 返回
       └─ miss → Redis L2 (远程, 毫秒级)
                   ├─ hit → 回填 Caffeine → 返回
                   └─ miss → DB → 回填 Caffeine + Redis → 返回

Redis 宕机时:
  请求 → Caffeine L1 hit → 返回（不受影响）
  请求 → Caffeine L1 miss → Redis 超时 → DB（有风险，需配合限流）

Redis 恢复后:
  自动恢复 L2 缓存，Caffeine 逐渐过期转向 Redis
```

> 更成熟的多级缓存方案：考虑直接引入 **JetCache**（阿里的 `@Cached` 注解原生支持 local + remote 两级）或 **Redisson** 的本地缓存。

---

## 方案三：缓存预热

**原理**：项目启动时（或 Redis 重启后），预先加载热点数据到缓存，避免"冷启动"造成雪崩。

### 预热组件

```java
package xyz.nanian.owl.sugarcane.component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import xyz.nanian.owl.sugarcane.domain.entity.CategoryDO;
import xyz.nanian.owl.sugarcane.domain.vo.CategoryTreeVO;
import xyz.nanian.owl.sugarcane.service.CategoryService;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * 缓存预热 — 应用启动完成后异步加载热点数据
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CacheWarmer {

    private final RedisTemplate<String, Object> redisTemplate;
    private final CategoryService categoryService;

    @EventListener(ApplicationReadyEvent.class)
    public void warmUp() {
        log.info("开始缓存预热...");
        CompletableFuture.runAsync(() -> {
            try {
                // 预热分类树
                List<CategoryTreeVO> tree = categoryService.getCategoryTree();
                redisTemplate.opsForValue().set("category:tree", tree, Duration.ofMinutes(30));

                log.info("缓存预热完成: category:tree({} 条)", tree.size());
            } catch (Exception e) {
                log.error("缓存预热失败", e);
            }
        });
    }
}
```

### 预热清单

| 预热数据 | 理由 |
|---------|------|
| `category:tree` | 全站每页必加载 |
| `source:*` | 所有价格查询都关联来源 |
| `geo:tree` | 价格对比页依赖 |

---

## 方案四：熔断降级（兜底）

**原理**：当 Redis 宕机、DB 压力过大时，服务直接降级返回兜底数据或空结果，**比把 DB 打死强**。

### 依赖

```xml
<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-spring-boot3</artifactId>
</dependency>
```

### 配置

```yaml
# application.yml
resilience4j:
  circuitbreaker:
    instances:
      priceLatest:
        sliding-window-type: COUNT_BASED
        sliding-window-size: 10
        failure-rate-threshold: 50        # 50% 失败 → 熔断
        wait-duration-in-open-state: 30s   # 熔断 30 秒后半开试探
        permitted-number-of-calls-in-half-open-state: 3
      priceTrend:
        sliding-window-size: 10
        failure-rate-threshold: 50
        wait-duration-in-open-state: 30s
  timelimiter:
    instances:
      priceLatest:
        timeout-duration: 3s               # 超时 3 秒即视为失败
```

### Service 使用

```java
@Service
@RequiredArgsConstructor
@Slf4j
public class RecordServiceImpl extends ServiceImpl<RecordMapper, RecordDO> implements RecordService {

    private final RecordMapper recordMapper;

    @Override
    @CircuitBreaker(name = "priceLatest", fallbackMethod = "queryLatestFallback")
    @TimeLimiter(name = "priceLatest")
    public PriceLatestVO queryLatest(PriceLatestQueryDTO dto) {
        // 正常：布隆过滤 → Redis → DB
        return recordMapper.selectLatest(dto.getItemId());
    }

    /** 熔断降级：返回 null 或静态兜底数据 */
    private PriceLatestVO queryLatestFallback(PriceLatestQueryDTO dto, Throwable t) {
        log.warn("priceLatest 熔断降级触发: {}", t.getMessage());
        return null;
    }
}
```

---

## 方案对比

| 方案 | 防什么 | 复杂度 | 额外依赖 | 效果 |
|------|--------|--------|---------|------|
| TTL 随机偏移 | 同时过期 | 极低 | 无 | 把瞬时压力摊平到一段时间 |
| 多级缓存 Caffeine | Redis 宕机 | 中 | caffeine | Redis 恢复前 Caffeine 扛 |
| 缓存预热 | 冷启动 | 低 | 无 | 启动后缓存已有数据 |
| 熔断降级 | 全部失守 | 中 | resilience4j | 阻止 DB 被打死 |

## 推荐组合

```
┌──────────────────────────────────────────┐
│ 第一层：TTL 随机偏移                       │  ← 成本最低，一行工具类
│   把同时过期打散，防"A 场景"                │
├──────────────────────────────────────────┤
│ 第二层：Caffeine L1 缓存                   │  ← 防 Redis 短暂不可用
│   参考数据（分类树、来源、地理）用本地缓存    │
├──────────────────────────────────────────┤
│ 第三层：缓存预热                           │  ← 防冷启动
│   ApplicationReady 时异步加载热点数据       │
├──────────────────────────────────────────┤
│ 第四层：熔断降级                           │  ← 兜底
│   全部失效时返回空，不让 DB 被打死           │
└──────────────────────────────────────────┘
```

### 实施顺序

1. **TTL 随机偏移** — 类就 10 行，先加上
2. **缓存预热** — `ApplicationReadyEvent` 异步加载热点
3. **Caffeine 多级缓存** — 改造成本略高，参考数据优先
4. **熔断降级** — 最后防线，需要时再加
