package xyz.nanian.owl.infrastructure.bloom.service;


/**
 * Bloom过滤器通用接口 — 防缓存穿透
 * 各业务模块注入此接口，向各自的命名空间写入 key
 *
 * @author slnt23
 * @since 2026/5/30
 */

public interface BloomFilterService {

    /**
     * 判断 key 是否可能存在（假阳性率 1%）
     */
    boolean mightContain(String key);

    /**
     * 新增 key（写入/更新时调用）
     */
    void add(String key);

    /**
     * 预估元素数量
     */
    long approximateCount();
}
