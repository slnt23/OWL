package xyz.nanian.owl.sugarcane.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import xyz.nanian.owl.infrastructure.bloom.service.BloomFilterService;
import xyz.nanian.owl.sugarcane.constant.CacheConstant;
import xyz.nanian.owl.sugarcane.domain.dto.PriceCompareLocationDTO;
import xyz.nanian.owl.sugarcane.domain.dto.PriceCompareSourceDTO;
import xyz.nanian.owl.sugarcane.domain.dto.PriceLatestQueryDTO;
import xyz.nanian.owl.sugarcane.domain.dto.PriceTrendQueryDTO;
import xyz.nanian.owl.sugarcane.domain.entity.RecordDO;
import xyz.nanian.owl.sugarcane.domain.vo.*;
import xyz.nanian.owl.sugarcane.mapper.RecordMapper;
import xyz.nanian.owl.sugarcane.service.RecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

import static xyz.nanian.owl.sugarcane.constant.CacheConstant.BLOOM_ITEM_PREFIX;

/**
 * <p>
 * 价格记录表（时间序列数据） 服务实现类
 * </p>
 *
 * @author slnt23
 * @since 2026-04-12 20:43:32
 */
@Service
@RequiredArgsConstructor
public class RecordServiceImpl extends ServiceImpl<RecordMapper, RecordDO> implements RecordService {

    final private RecordMapper recordMapper;
    private final BloomFilterService bloomFilter;

    @Override
    @Cacheable(value = CacheConstant.PRICE_LATEST, key = "#dto.itemId", sync = true)
    public PriceLatestVO queryLatest(PriceLatestQueryDTO dto) {

        if (dto.getItemId() != null && !bloomFilter.mightContain(BLOOM_ITEM_PREFIX + dto.getItemId())) {
            return null;
        }

        PriceLatestVO vo = recordMapper.selectLatest(dto.getItemId());
        return vo;
    }

    @Override
    @Cacheable(value = CacheConstant.PRICE_TREND, key = "#dto.cacheKey()", sync = true)
    public List<PriceTrendVO> queryTrend(PriceTrendQueryDTO dto) {

        if (dto.getItemId() != null && !bloomFilter.mightContain(BLOOM_ITEM_PREFIX + dto.getItemId())) {
            return null;
        }
        return recordMapper.selectTrend(dto);
    }

    @Override
    @Cacheable(value = CacheConstant.PRICE_COMPARE_LOCATION, key = "#dto.cacheKey()", sync = true)
    public PriceCompareVO compareLocation(PriceCompareLocationDTO dto) {

        if (dto.getItemId() != null && !bloomFilter.mightContain(BLOOM_ITEM_PREFIX + dto.getItemId())) {
            return null;
        }
        // 查询物品信息
        PriceItemVO item = recordMapper.selectItemByIdOrCode(dto.getItemId(), dto.getItemCode());

        // 查询指定时间的各地区价格
        List<LocationPriceVO> prices = recordMapper.selectLocationPrices(dto);

        PriceCompareVO vo = new PriceCompareVO();
        vo.setItem(item);
        vo.setPrices(prices);
        return vo;
    }

    @Override
    @Cacheable(value = CacheConstant.PRICE_COMPARE_SOURCE, key = "#dto.cacheKey()", sync = true)
    public List<SourceCompareVO> compareSource(PriceCompareSourceDTO dto) {
        if (dto.getItemId() != null && !bloomFilter.mightContain(BLOOM_ITEM_PREFIX + dto.getItemId())) {
            return null;
        }
        return recordMapper.selectSourcePrices(dto);
    }
}
