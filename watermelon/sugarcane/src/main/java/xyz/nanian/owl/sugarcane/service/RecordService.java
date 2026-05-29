package xyz.nanian.owl.sugarcane.service;

import xyz.nanian.owl.sugarcane.domain.dto.PriceCompareLocationDTO;
import xyz.nanian.owl.sugarcane.domain.dto.PriceCompareSourceDTO;
import xyz.nanian.owl.sugarcane.domain.dto.PriceLatestQueryDTO;
import xyz.nanian.owl.sugarcane.domain.dto.PriceTrendQueryDTO;
import xyz.nanian.owl.sugarcane.domain.entity.RecordDO;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.nanian.owl.sugarcane.domain.vo.PriceCompareVO;
import xyz.nanian.owl.sugarcane.domain.vo.PriceLatestVO;
import xyz.nanian.owl.sugarcane.domain.vo.PriceTrendVO;
import xyz.nanian.owl.sugarcane.domain.vo.SourceCompareVO;

import java.util.List;

/**
 * <p>
 * 价格记录表（时间序列数据） 服务类
 * </p>
 *
 * @author slnt23
 * @since 2026-04-12 20:43:32
 */
public interface RecordService extends IService<RecordDO> {

    /**
     * 最新价格
     *
     * @param dto
     * @return
     */
    PriceLatestVO queryLatest(PriceLatestQueryDTO dto);

    /**
     * 查询趋势
     *
     * @param dto
     * @return
     */
    List<PriceTrendVO> queryTrend(PriceTrendQueryDTO dto);

    /**
     * 地区对比
     *
     * @param dto
     * @return
     */
    PriceCompareVO compareLocation(PriceCompareLocationDTO dto);

    /**
     * 多来源对比
     *
     * @param dto
     * @return
     */
    List<SourceCompareVO> compareSource(PriceCompareSourceDTO dto);
}
