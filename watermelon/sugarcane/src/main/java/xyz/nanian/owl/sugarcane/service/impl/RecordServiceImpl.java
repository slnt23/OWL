package xyz.nanian.owl.sugarcane.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import xyz.nanian.owl.log.annotation.OperationLog;
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

    @Override
    @OperationLog(module = "Record", action = "查询最新价格信息")
    @Cacheable(value = CacheConstant.PRICE_LATEST, key = "#dto.cacheKey()", sync = true)
    public PriceLatestVO queryLatest(PriceLatestQueryDTO dto) {
        PriceLatestVO vo = recordMapper.selectLatest(dto.getItemId(), dto.getLocationId(), dto.getCurrency());
        return vo;
    }

    @Override
    @OperationLog(module = "Record", action = "查询物品价格趋势")
    @Cacheable(value = CacheConstant.PRICE_TREND, key = "#dto.cacheKey()", sync = true)
    public List<PriceTrendVO> queryTrend(PriceTrendQueryDTO dto) {
        return recordMapper.selectTrend(dto);
    }

    @Override
    @OperationLog(module = "Record", action = "地区对比")
    @Cacheable(value = CacheConstant.PRICE_COMPARE_LOCATION, key = "#dto.cacheKey()", sync = true)
    public PriceCompareVO compareLocation(PriceCompareLocationDTO dto) {
        // 查询物品信息
        PriceItemVO item = recordMapper.selectItemByIdOrCode(dto.getItemId(), dto.getItemCode());

        // 查询指定时间的各地区价格
        List<LocationPriceVO> prices = recordMapper.selectLocationPrices(
                dto.getItemId(),
                dto.getLocationId(),
                dto.getTargetTime());

        PriceCompareVO vo = new PriceCompareVO();
        vo.setItem(item);
        vo.setPrices(prices);
        return vo;
    }

    @Override
    @OperationLog(module = "Record", action = "来源对比")
    @Cacheable(value = CacheConstant.PRICE_COMPARE_SOURCE, key = "#dto.cacheKey()", sync = true)
    public List<SourceCompareVO> compareSource(PriceCompareSourceDTO dto) {
        return recordMapper.selectSourcePrices(dto);
    }
}
