package xyz.nanian.owl.sugarcane.service;

import xyz.nanian.owl.sugarcane.domain.dto.PriceLatestQueryDTO;
import xyz.nanian.owl.sugarcane.domain.entity.RecordDO;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.nanian.owl.sugarcane.domain.vo.PriceLatestVO;

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
}
