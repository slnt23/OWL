package xyz.nanian.owl.user.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.log.logging.BizLog;
import xyz.nanian.owl.user.domain.dto.UserInfoDTO;
import xyz.nanian.owl.user.domain.entity.UserDO;
import xyz.nanian.owl.user.mapper.UserMapper;
import xyz.nanian.owl.user.mapstruct.UserConvert;
import xyz.nanian.owl.user.service.UserService;

//import static xyz.nanian.owl.user.mapstruct.UserConvert.INSTANCE;

/**
 * 用户相关的逻辑类实现
 *
 * @author slnt23
 * @since 2025/11/13
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper ;
    private final PasswordEncoder passwordEncoder;
    private final UserConvert userConvert;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, Object> redisTemplate;


//    UserServiceImpl(UserMapper userMapper,
//                    PasswordEncoder passwordEncoder,
//                    UserConvert userConvert,
//                    StringRedisTemplate stringRedisTemplate,
//                    RedisTemplate<String, Object> redisTemplate) {
//
//        this.userMapper = userMapper ;
//        this.passwordEncoder = passwordEncoder;
//        this.userConvert = userConvert;
//        this.stringRedisTemplate = stringRedisTemplate;
//        this.redisTemplate = redisTemplate;
//    }


//    /**
//     * 用户注册
//     * @param sendCodeDTO 用户DTO基本信息
//     */
//    @Override
//    @BizLog(module = "用户",action = "注册用户")
//    public Boolean saveUser(SendCodeDTO sendCodeDTO) {
//
//        UserDO userDO= userConvert.registerDTOToUserDO(sendCodeDTO);
//
//        设定账号
//        String userCode = UUID.randomUUID().toString();
//        userDO.setUserCode(userCode);
//
//        加密密码
//        String password = userDO.getPassword();
//        String encoded= passwordEncoder.encode(password);
//        userDO.setPassword(encoded);
//
//        userMapper.insert(userDO);
//
//        return true;
//    }
//    /**
//     * 用户登陆
//     * @param phone 手机号
//     * @param password 输入的密码
//     * @return 密码是否正确的 bool
//     */
//    @Override
//    @BizLog(module = "用户",action = "用户登陆")
//    public String login(String phone, String password) {
//
//         从redis查用户
//        String key = LOGIN_KEY + phone;
//        UserDO userDO= (UserDO) redisTemplate.opsForValue().get(key);
//
//        Redis没有，从数据库查并写缓存，
//        if (userDO==null) {
//            userDO = userMapper.select(phone);
//            if (userDO==null) {
//                return null;
//            }
//            redisTemplate.opsForValue().set(key,userDO,LOGIN_TIME_OUT,TimeUnit.DAYS);
//        }
//
//        校验
//        if(passwordEncoder.matches(password,userDO.getPassword())) {
//            JWT由userId，userPhone
//            return JwtUtil.generateToken(userDO.getId(),userDO.getPhone());
//        }
//        return null;
//    }

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
