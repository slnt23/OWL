package xyz.nanian.owl.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.nanian.owl.admin.domain.entity.UserDO;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author slnt23
 * @since 2026-04-13 23:53:18
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

}
