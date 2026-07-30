package xyz.nanian.owl.sugarcane.component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import xyz.nanian.owl.infra.bloom.service.BloomFilterService;
import xyz.nanian.owl.sugarcane.domain.entity.ItemDO;
import xyz.nanian.owl.sugarcane.service.ItemService;

import java.util.List;

import static xyz.nanian.owl.sugarcane.constant.CacheConstant.BLOOM_ITEM_PREFIX;

/**
 * sugarcane 模块布隆过滤器初始化
 * 启动时将所有 itemId 注册进去
 *
 * @author slnt23
 * @since 2026/5/30
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
            bloomFilter.add(BLOOM_ITEM_PREFIX + item.getId());
        }
    }
}
