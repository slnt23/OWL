package xyz.nanian.owl.sugarcane.mapper;

import xyz.nanian.owl.sugarcane.entity.RecordDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 价格记录表（时间序列数据） Mapper 接口
 * </p>
 *
 * @author slnt23
 * @since 2026-04-12 20:43:32
 */
@Mapper
public interface RecordMapper extends BaseMapper<RecordDO> {

}
