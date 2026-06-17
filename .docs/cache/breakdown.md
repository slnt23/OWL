# sugarcane 缓存击穿解决方案

## 什么是缓存击穿

某个**热点 key** 在过期的瞬间，大量并发请求同时穿透缓存打到 DB，造成 DB 瞬时压力飙升。

与穿透的区别：穿透是查**不存在**的数据，击穿是查**存在且热门**的数据，只是缓存刚好过期了。

## sugarcane 风险点

| 热点 key | 触发条件 |
|----------|---------|
| `category:tree` | 分类树被全站引用，过期瞬间可能数百 QPS 打到 DB |
| `price:latest:{热门itemId}` | 油价等热门商品高频刷新查询 |
| `source:list` | 来源列表多处依赖 |

---

## 方案一：互斥锁（SETNX）

**原理**：key 过期时，只让**一个请求**去查 DB 并重建缓存，其他请求自旋等待，缓存重建后直接返回。

**适用**：对数据一致性要求高的场景（价格查询）。

### 组件代码

```java
package xyz.nanian.owl.sugarcane.component;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.function.Supplier;

/**
 * 防缓存击穿的互斥锁工具
 *
 * 用法：
 *   PriceLatestVO result = cacheMutex.getWithMutex(
 *       "price:latest:" + itemId,
 *       () -> recordMapper.selectLatest(itemId),
 *       Duration.ofMinutes(1)
 *   );
 */
@Component
@RequiredArgsConstructor
public class CacheMutex {

    private final RedisTemplate<String, Object> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    /** 锁超时时间（防死锁） */
    private static final long LOCK_TIMEOUT_SECONDS = 10;
    /** 自旋重试间隔 */
    private static final long RETRY_INTERVAL_MS = 50;

    /**
     * 带互斥锁的缓存查询，防止热点 key 过期时并发打 DB
     */
    @SuppressWarnings("unchecked")
    public <T> T getWithMutex(String cacheKey, Supplier<T> loader, Duration ttl) {
        // 1. 先查缓存
        Object cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            return (T) cached;
        }

        String lockKey = "mutex:" + cacheKey;

        try {
            // 2. 自旋获取锁
            while (!tryLock(lockKey)) {
                Thread.sleep(RETRY_INTERVAL_MS);
                // 等待期间再次查缓存（其他线程可能已重建完毕）
                cached = redisTemplate.opsForValue().get(cacheKey);
                if (cached != null) {
                    return (T) cached;
                }
            }

            // 3. 获取到锁 → 二次检查缓存 → 查 DB → 写缓存
            cached = redisTemplate.opsForValue().get(cacheKey);
            if (cached != null) {
                return (T) cached;
            }

            T result = loader.get();
            if (result != null) {
                redisTemplate.opsForValue().set(cacheKey, result, ttl);
            }
            return result;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return loader.get(); // 降级：直接查 DB
        } finally {
            stringRedisTemplate.delete(lockKey);
        }
    }

    private boolean tryLock(String key) {
        Boolean ok = stringRedisTemplate.opsForValue()
                .setIfAbsent(key, "1", Duration.ofSeconds(LOCK_TIMEOUT_SECONDS));
        return Boolean.TRUE.equals(ok);
    }
}
```

### Service 使用

```java
@Service
@RequiredArgsConstructor
public class RecordServiceImpl extends ServiceImpl<RecordMapper, RecordDO> implements RecordService {

    private final RecordMapper recordMapper;
    private final CacheMutex cacheMutex;

    @Override
    public PriceLatestVO queryLatest(PriceLatestQueryDTO dto) {
        String key = "price:latest:" + dto.getItemId();
        return cacheMutex.getWithMutex(key,
                () -> recordMapper.selectLatest(dto.getItemId()),
                Duration.ofMinutes(1));
    }
}
```

### 执行时序

```
请求1 ─→ 查缓存(miss) ─→ 获取锁(成功) ─→ 查DB ─→ 写缓存 ─→ 释放锁 ─→ 返回
请求2 ─→ 查缓存(miss) ─→ 获取锁(失败) ─→ sleep(50ms) ─→ 查缓存(hit!) ─→ 返回
请求3 ─→ 查缓存(miss) ─→ 获取锁(失败) ─→ sleep(50ms) ─→ 查缓存(hit!) ─→ 返回
请求4 ─→ 查缓存(miss) ─→ 获取锁(失败) ─→ sleep(50ms) ─→ 查缓存(hit!) ─→ 返回
```

N 个并发请求，只有 1 个查了 DB。

---

## 方案二：逻辑过期

**原理**：缓存**不设 Redis TTL**（永不过期），在 value 中嵌入一个逻辑过期时间字段。读缓存时：
- 未过期 → 直接返回
- 已过期 → **立即返回旧值**，同时异步开线程去重建缓存

**适用**：高并发、允许短暂数据不一致的场景（分类树、来源列表、地理数据）。

### 组件代码

```java
package xyz.nanian.owl.sugarcane.component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * 逻辑过期缓存 — 热点 key 过期时不阻塞用户，返回旧值 + 异步重建
 *
 * 用法：
 *   List<CategoryTreeVO> tree = logicalExpireCache.getWithLogicalExpire(
 *       "category:tree",
 *       Duration.ofMinutes(30),   // 逻辑 TTL
 *       () -> categoryService.buildTree()
 *   );
 */
@Component
@RequiredArgsConstructor
public class LogicalExpireCache {

    private final RedisTemplate<String, Object> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    private static final ExecutorService REBUILD_POOL = Executors.newFixedThreadPool(4);

    /** 包装数据 + 逻辑过期时间（存入 Redis 的 value） */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CacheWrapper<T> {
        private T data;
        private LocalDateTime expireAt;

        public static <T> CacheWrapper<T> of(T data, Duration ttl) {
            return new CacheWrapper<>(data, LocalDateTime.now().plus(ttl));
        }

        @JsonIgnore
        public boolean isExpired() {
            return LocalDateTime.now().isAfter(expireAt);
        }
    }

    /**
     * 逻辑过期查询：过期时返回旧值，异步重建
     */
    @SuppressWarnings("unchecked")
    public <T> T getWithLogicalExpire(String key, Duration ttl, Supplier<T> loader) {
        Object cached = redisTemplate.opsForValue().get(key);

        // 首次无缓存 → 同步加载
        if (cached == null) {
            synchronized (this) {
                cached = redisTemplate.opsForValue().get(key);
                if (cached != null) {
                    return ((CacheWrapper<T>) cached).getData();
                }
                T data = loader.get();
                if (data != null) {
                    redisTemplate.opsForValue().set(key, CacheWrapper.of(data, ttl));
                }
                return data;
            }
        }

        CacheWrapper<T> wrapper = (CacheWrapper<T>) cached;

        // 未过期 → 直接返回
        if (!wrapper.isExpired()) {
            return wrapper.getData();
        }

        // 已过期 → 返回旧值 + 异步重建
        String lockKey = "mutex:" + key;
        REBUILD_POOL.submit(() -> {
            try {
                Boolean ok = stringRedisTemplate.opsForValue()
                        .setIfAbsent(lockKey, "1", Duration.ofSeconds(10));
                if (!Boolean.TRUE.equals(ok)) return; // 已有线程在重建

                T newData = loader.get();
                if (newData != null) {
                    redisTemplate.opsForValue().set(key, CacheWrapper.of(newData, ttl));
                }
            } finally {
                stringRedisTemplate.delete(lockKey);
            }
        });

        return wrapper.getData(); // 返回旧值，不阻塞
    }
}
```

### Service 使用

```java
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryDO> implements CategoryService {

    private final LogicalExpireCache logicalExpireCache;

    public List<CategoryTreeVO> getCategoryTree() {
        return logicalExpireCache.getWithLogicalExpire(
                "category:tree",
                Duration.ofMinutes(30),
                this::buildTree);   // DB 查询逻辑
    }

    private List<CategoryTreeVO> buildTree() {
        List<CategoryDO> all = list(new LambdaQueryWrapper<CategoryDO>()
                .eq(CategoryDO::getStatus, (byte) 1));
        // ... 递归构建树
        return treeList;
    }
}
```

### 执行时序

```
请求1 ─→ 查缓存(wrapper已过期) ─→ 返回旧数据(不阻塞!)
                                    └→ 异步线程：获取锁 → 查DB → 写缓存 → 释放锁
请求2 ─→ 查缓存(wrapper已过期) ─→ 返回旧数据(不阻塞!)
请求3 ─→ 查缓存(wrapper已过期) ─→ 返回旧数据(不阻塞!)
```

所有请求无阻塞，用户无感知。重建完成后下次请求拿到新数据。

---

## 方案三：Spring Cache `sync = true`

**原理**：`@Cacheable` 的 `sync` 属性为同一 key 的并发请求加锁，只有一个执行方法体，其余等待结果。

**最简单**，但只防单 JVM 内的并发，多实例各自会查一次 DB。

```java
@Override
@Cacheable(value = "categoryTree", key = "'tree'", sync = true)
public List<CategoryTreeVO> getCategoryTree() {
    return buildTree();
}
```

前提：`RedisCacheManager` 不能配置 `disableCachingNullValues()`，否则 sync 模式下 null 不缓存导致等锁的请求白等。

---

## 方案对比

| 维度 | 互斥锁 | 逻辑过期 | sync=true |
|------|--------|---------|-----------|
| 线程行为 | 等锁的线程自旋阻塞 | 不阻塞，返回旧值 | 等锁的线程阻塞 |
| 数据实时性 | 强一致（立即拿到新数据） | 最终一致（下一次请求拿到） | 强一致 |
| 用户体验 | 等待 50~200ms | 无感知 | 等待 |
| 实现复杂度 | 中 | 中 | 极低 |
| 多实例 | 安全（Redis 锁跨实例） | 安全 | **不安全**（各自查一次 DB） |
| 死锁风险 | 有（靠 TTL 兜底） | 无 | 无 |
| 适用场景 | 价格查询 | 分类树、来源列表 | 低频热点、单实例 |

### 推荐

- **价格类查询**（`price:latest`）→ 互斥锁，用户愿意等几十毫秒换强一致
- **参考数据**（`category:tree`、`source:list`、`geo:*`）→ 逻辑过期，数据变化极少，用户不能等
- **简单场景** → `sync = true`，一行搞定
