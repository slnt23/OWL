package xyz.nanian.owl.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.nanian.owl.user.domain.entity.UserRoleDO;

/**
 * <p>
 * 用户角色关联表 Mapper 接口
 * </p>
 *
 * @author slnt23
 * @since 2026-04-19 13:02:36
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRoleDO> {

}
