package xyz.nanian.owl.user.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.logging.BizLog;
import xyz.nanian.owl.user.dto.UserInfoDTO;
import xyz.nanian.owl.user.dto.UserRegisterDTO;
import xyz.nanian.owl.user.entity.UserDO;
import xyz.nanian.owl.user.mapper.UserMapper;
import xyz.nanian.owl.user.mapstruct.UserConvert;
import xyz.nanian.owl.user.service.UserService;

import java.util.UUID;

//import static xyz.nanian.owl.user.mapstruct.UserConvert.INSTANCE;

/**
 * 用户相关的逻辑类实现
 *
 * @author slnt23
 * @since 2025/11/13
 */

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper ;
    private final PasswordEncoder passwordEncoder;
    private final UserConvert userConvert;

    /**
     * 构造器注入
     * @param userMapper 用户Mapper
     * @param passwordEncoder 密码编码器
     */
    UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder, UserConvert userConvert) {

        this.userMapper = userMapper ;
        this.passwordEncoder = passwordEncoder;
        this.userConvert = userConvert;
    }


    /**
     * 用户注册接口,
     * @param userRegisterDTO 用户DTO基本信息
     */
    @Override
    @BizLog(module = "用户",action = "注册用户")
    public void saveUser(UserRegisterDTO userRegisterDTO) {

        UserDO userDO= userConvert.registerDTOToUserDO(userRegisterDTO);

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
    @BizLog(module = "用户",action = "用户登陆")
    public UserInfoDTO login(String phone, String password) {

        UserDO userDO= userMapper.select(phone);

        String rawPassword = userDO.getPassword();
        if(passwordEncoder.matches(password,rawPassword)){
            return userConvert.userDOToUserInfoDTO(userDO);
        }else {
            return null;
        }
    }

    /**
     * 更新用户信息
     * 现在是根据手机号找到用户信息,
     *
     * @param userInfoDTO 用户DTO
     * @return 更新是否成功的bool
     */
    @Override
    @BizLog(module = "用户",action = "更新用户信息")
    public Boolean updateUserInfo(UserInfoDTO userInfoDTO) {

        UserDO userDO =userMapper.select(userInfoDTO.getRawPhone());
        UserDO user = userConvert.UserInfoToUserDO(userInfoDTO);

        user.setUserCode(userDO.getUserCode());

        return userMapper.update(user);
    }

    /**
     * 更新用户密码
     * @param phone 原手机号
     * @param newPassword 新密码
     * @return bool
     */
    @Override
    @BizLog(module = "用户",action = "更新用户密码")
    public Boolean updateUserPassword(String phone, String newPassword) {

        UserDO user=userMapper.select(phone);
        String encryptedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encryptedPassword);

        return userMapper.update(user);
    }


}
