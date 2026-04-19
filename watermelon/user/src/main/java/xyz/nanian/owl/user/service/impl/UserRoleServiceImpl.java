package xyz.nanian.owl.user.service.impl;

import xyz.nanian.owl.user.domain.entity.UserRoleDO;
import xyz.nanian.owl.user.mapper.UserRoleMapper;
import xyz.nanian.owl.user.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户角色关联表 服务实现类
 * </p>
 *
 * @author slnt23
 * @since 2026-04-19 13:02:36
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleDO> implements UserRoleService {

}
