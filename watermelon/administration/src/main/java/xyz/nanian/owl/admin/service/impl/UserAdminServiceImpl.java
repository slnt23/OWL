package xyz.nanian.owl.admin.service.impl;

import xyz.nanian.owl.admin.domain.entity.UserDO;
import xyz.nanian.owl.admin.mapper.UserAdminMapper;
import xyz.nanian.owl.admin.service.UserAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author slnt23
 * @since 2026-04-13 23:53:18
 */
@Service
public class UserAdminServiceImpl extends ServiceImpl<UserAdminMapper, UserDO> implements UserAdminService {

}
