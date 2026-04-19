package xyz.nanian.owl.user.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.nanian.owl.infrastructure.minio.constant.MinioConstant;
import xyz.nanian.owl.infrastructure.minio.service.impl.MinioFileServiceImpl;
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
    private final MinioFileServiceImpl minioFileServiceImpl;

    /**
     * 更新用户信息
     * 现在是根据手机号找到用户信息, TODO 这里改为使用UserCode来更新用户信息, 因为手机号可能会变更, 但是UserCode是唯一且不变的
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

    /**
     * 更新用户头像
     * @param file
     * @param userCode
     * @return
     */
    @SneakyThrows
    @Override
    public String updateUserAvatar(MultipartFile file, String userCode) {

        String avatarUrl = minioFileServiceImpl.upload(file, MinioConstant.BUCKET_AVATARS);

        UserDO userDO = new UserDO();
        userDO.setUserCode(userCode);
        userDO.setAvatar(avatarUrl);

        boolean success = userMapper.update(userDO);

        if (success) {
            return avatarUrl;
        }else {
            throw new Exception("更新用户头像失败");
        }
    }

}
