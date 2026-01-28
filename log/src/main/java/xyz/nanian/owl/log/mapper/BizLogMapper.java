package xyz.nanian.owl.log.mapper;


import xyz.nanian.owl.log.DO.BizLogDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 业务日志Mapper
 *
 * @author slnt23
 * @since 2026/1/27
 */

@Mapper
public interface BizLogMapper extends BaseMapper<BizLogDO> {
}
