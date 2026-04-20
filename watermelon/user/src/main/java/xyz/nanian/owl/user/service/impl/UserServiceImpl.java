package xyz.nanian.owl.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.nanian.owl.infrastructure.minio.constant.MinioConstant;
import xyz.nanian.owl.infrastructure.minio.service.FileStorageService;
import xyz.nanian.owl.log.logging.BizLog;
import xyz.nanian.owl.user.domain.dto.UserInfoDTO;
import xyz.nanian.owl.user.domain.entity.UserDO;
import xyz.nanian.owl.user.mapper.UserMapper;
import xyz.nanian.owl.user.mapstruct.UserConvert;
import xyz.nanian.owl.user.service.UserService;
import xyz.nanian.owl.utils.jwt.UserContext;


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
    private final FileStorageService fileStorageService;


    /**
     * 更新用户信息
     *
     * @param userInfoDTO 用户DTO
     * @return 更新是否成功的bool
     */
    @Override
    @BizLog(module = "用户",action = "更新用户信息")
    public Boolean updateUserInfo(UserInfoDTO userInfoDTO) {

//        1. 获取用户信息，
        String userCode = UserContext.getUserCode();

//        这里还是有商讨，万一传过来的是空的，岂不是覆盖原有的值，
        UserDO user = userConvert.UserInfoToUserDO(userInfoDTO);
        user.setUserCode(userCode);

//        LambdaUpdateWrapper<UserDO> wrapper = new LambdaUpdateWrapper<>();
//        wrapper.eq(UserDO::getUserCode, userCode);
//
//        int result = userMapper.update(user, wrapper);
//        用下面的，上面的是单独的，下面的是总的，

        return userMapper.update(user) > 0;
    }

    /**
     * 更新用户密码
     * @param newPassword 新密码
     * @return bool
     */
    @Override
    @BizLog(module = "用户",action = "更新用户密码")
    public Boolean updateUserPassword(String newPassword) {

        String userCode = UserContext.getUserCode();

        String encryptedPassword = passwordEncoder.encode(newPassword);
        UserDO user = new UserDO();
        user.setPassword(encryptedPassword);

        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<UserDO>();
        wrapper.eq(UserDO::getUserCode,userCode);

        int result = userMapper.update(user,wrapper);

        return result == 1;
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

        String avatarUrl = fileStorageService.upload(file, MinioConstant.BUCKET_AVATARS);

        UserDO userDO = new UserDO();
        userDO.setUserCode(userCode);
        userDO.setAvatar(avatarUrl);

        int success = userMapper.update(userDO);

        if (success> 0) {
            return avatarUrl;
        }else {
            throw new Exception("更新用户头像失败");
        }
    }

}
