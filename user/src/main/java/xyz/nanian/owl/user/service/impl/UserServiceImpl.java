package xyz.nanian.owl.user.service.impl;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.user.dto.UserInfoDTO;
import xyz.nanian.owl.user.dto.UserRegisterDTO;
import xyz.nanian.owl.user.entity.UserDO;
import xyz.nanian.owl.user.mapper.UserMapper;
import xyz.nanian.owl.user.service.UserService;

import java.util.UUID;

import static xyz.nanian.owl.user.mapstruct.UserMap.INSTANCE;

/**
 * 用户相关的逻辑类实现
 *
 * @author slnt23
 * @since 2025/11/13
 */

@Service
public class UserServiceImpl implements UserService {

    final UserMapper userMapper ;
    private final PasswordEncoder passwordEncoder;


    /**
     * 构造器注入
     * @param userMapper 用户Mapper
     * @param passwordEncoder 密码编码器
     */
    UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder) {

        this.userMapper = userMapper ;
        this.passwordEncoder = passwordEncoder;
    }



    /**
     * 用户注册接口,
     * @param userRegisterDTO 用户DTO基本信息
     */
    @Override
    public void saveUser(UserRegisterDTO userRegisterDTO) {

        UserDO userDO= INSTANCE.registerToUserDO(userRegisterDTO);

//        设定账号
        String userCode = UUID.randomUUID().toString();
        userDO.setUserCode(userCode);

//        加密密码
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

//        String rawPassword = userMapper.selectPasswordByPhone(phone);

        UserDO userDO= userMapper.select(phone);
        String rawPassword = userDO.getPassword();
        return passwordEncoder.matches(password,rawPassword);//明文密码在前，
    }

    /**
     * 更新用户信息，这里我的思路是，先根据已经有的邮箱或者手机号，查询到账号，
     * 再根据账号更改信息，
     * @param userInfoDTO 用户DTO
     * @return 更新是否成功的bool
     */
    @Override
    public Boolean updateUserInfo(UserInfoDTO userInfoDTO,String rawPhone) {

//        String userCode = userMapper.selectUserCodeByPhone(rawPhone);

        UserDO user=userMapper.select(rawPhone);
        String userCode =user.getUserCode();

        UserDO userDO=INSTANCE.updateUserDO(userInfoDTO);
        UserDO newUserDO=INSTANCE.updateUserDO(userDO);
        newUserDO.setUserCode(userCode);
        return userMapper.update(newUserDO);
    }

    /**
     * 更新用户密码
     * @param phone 原手机号
     * @param newPassword 新密码
     * @return bool
     */
    @Override
    public Boolean updateUserPassword(String phone, String newPassword) {

        UserDO user=userMapper.select(phone);
        String encryptedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encryptedPassword);

        return userMapper.update(user);
    }


}
