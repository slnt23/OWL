package xyz.nanian.owl.infrastructure.redis.service.Impl;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.infrastructure.redis.service.BloomFilterService;

import java.nio.charset.StandardCharsets;

/**
 * 实现基于 Guava 的内存布隆过滤器
 * 优点：零网络开销、纳秒级判断  |  缺点：重启需重建、多实例各自维护
 * <p>
 * 预估 100 万条 key，假阳性率 1%，内存 ≈ 1.2 MB
 *
 * @author slnt23
 * @since 2026/5/30
 */

@Service
public class GuavaBloomFilterServiceImpl implements BloomFilterService {

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
