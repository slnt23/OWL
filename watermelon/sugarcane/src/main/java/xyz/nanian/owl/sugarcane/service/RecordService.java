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

    PriceLatestVO queryLatest(PriceLatestQueryDTO dto);

    List<PriceTrendVO> queryTrend(PriceTrendQueryDTO dto);

    PriceCompareVO compareLocation(PriceCompareLocationDTO dto);

    List<SourceCompareVO> compareSource(PriceCompareSourceDTO dto);
}
