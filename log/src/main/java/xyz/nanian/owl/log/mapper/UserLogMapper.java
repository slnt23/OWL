package xyz.nanian.owl.log.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.nanian.owl.log.domain.entity.UserLogDO;

@Mapper
public interface UserLogMapper extends BaseMapper<UserLogDO> {
}
