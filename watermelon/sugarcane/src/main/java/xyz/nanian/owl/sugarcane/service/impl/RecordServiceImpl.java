package xyz.nanian.owl.sugarcane.service.impl;

import lombok.RequiredArgsConstructor;
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

    /**
     * 最新价格
     * @param dto
     * @return
     */
    @Override
    public PriceLatestVO queryLatest(PriceLatestQueryDTO dto) {

        PriceLatestVO vo = recordMapper.selectLatest(dto.getItemId());
        return vo;
    }

    /**
     * 查询趋势
     * @param dto
     * @return
     */
    @Override
    public List<PriceTrendVO> queryTrend(PriceTrendQueryDTO dto) {
        return recordMapper.selectTrend(dto);
    }

    /**
     * 地区对比
     * @param dto
     * @return
     */
    @Override
    public PriceCompareVO compareLocation(PriceCompareLocationDTO dto) {
        // 查询物品信息
        PriceItemVO item = recordMapper.selectItemByIdOrCode(dto.getItemId(), dto.getItemCode());

        // 查询指定时间的各地区价格
        List<LocationPriceVO> prices = recordMapper.selectLocationPrices(dto);

        PriceCompareVO vo = new PriceCompareVO();
        vo.setItem(item);
        vo.setPrices(prices);
        return vo;
    }


    /**
     * 多来源对比
     * @param dto
     * @return
     */
    @Override
    public List<SourceCompareVO> compareSource(PriceCompareSourceDTO dto) {
        return recordMapper.selectSourcePrices(dto);
    }
}
