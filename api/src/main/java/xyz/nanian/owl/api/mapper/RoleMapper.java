package xyz.nanian.owl.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.nanian.owl.api.domain.entity.RoleDO;

/**
 * 共享角色 Mapper，登录校验与后台角色管理共用。
 */
@Mapper
public interface RoleMapper extends BaseMapper<RoleDO> {
}
