package xyz.nanian.owl.user.service.impl;


import jakarta.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.user.dto.UserRegisterDTO;
import xyz.nanian.owl.user.entity.UserDO;
import xyz.nanian.owl.user.mapper.UserMapper;
import xyz.nanian.owl.user.service.UserService;

import static xyz.nanian.owl.user.mapstruct.UserMap.INSTANCE;

/**
 * 用户相关的逻辑类实现
 *
 * @author slnt23
 * @since 2025/11/13
 */

@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper ;
    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * 用户注册接口,
     * @param userRegisterDTO 用户DTO基本信息
     */
    @Override
    public void saveUser(UserRegisterDTO userRegisterDTO) {

        UserDO userDO= INSTANCE.registerToUserDO(userRegisterDTO);


        String password = userDO.getPassword();
        String encoded= passwordEncoder.encode(password);
        userDO.setPassword(encoded);

        userMapper.insert(userDO);

    }

    /**
     * 登陆验证
     * @param phone 手机号
     * @param password 输入的初始密码
     * @return 密码是否正确的 bool
     */
    @Override
    public Boolean login(String phone, String password) {

        String rawPassword = userMapper.selectPasswordByPhone(phone);
        return passwordEncoder.matches(rawPassword, password);
    }



}
