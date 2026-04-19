package xyz.nanian.owl.user.service.impl;

import xyz.nanian.owl.user.domain.entity.UserAddressDO;
import xyz.nanian.owl.user.mapper.UserAddressMapper;
import xyz.nanian.owl.user.service.UserAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户收货地址表 服务实现类
 * </p>
 *
 * @author slnt23
 * @since 2026-04-19 13:02:36
 */
@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddressDO> implements UserAddressService {

}
