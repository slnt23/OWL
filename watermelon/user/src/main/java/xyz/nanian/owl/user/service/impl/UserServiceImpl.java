package xyz.nanian.owl.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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
import xyz.nanian.owl.user.domain.vo.UserInfoVO;
import xyz.nanian.owl.user.mapper.RoleMapper;
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

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserConvert userConvert;
    private final FileStorageService fileStorageService;
    private final RoleMapper roleMapper;

    @Override
    @BizLog(module = "用户", action = "更新用户信息")
    public Boolean updateUserInfo(UserInfoDTO userInfoDTO) {

//        1. 获取用户信息，
        String userCode = UserContext.getUserCode();

//        2. 防止空值覆盖，
        LambdaUpdateWrapper<UserDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserDO::getUserCode, userCode);

        // 只更新非 null 字段，注意字段对应关系
        if (userInfoDTO.getUserName() != null) {
            wrapper.set(UserDO::getUserName, userInfoDTO.getUserName());
        }
        if (userInfoDTO.getEmail() != null) {
            wrapper.set(UserDO::getEmail, userInfoDTO.getEmail());
        }
        if (userInfoDTO.getPhone() != null) {
            wrapper.set(UserDO::getPhone, userInfoDTO.getPhone());
        }
        if (userInfoDTO.getNickname() != null) {
            wrapper.set(UserDO::getNickname, userInfoDTO.getNickname());  // 对应数据库 nickname
        }
        if (userInfoDTO.getRemark() != null) {
            wrapper.set(UserDO::getRemark, userInfoDTO.getRemark());
        }

        int result = userMapper.update(null, wrapper);
        return result > 0;
    }

    @Override
    @BizLog(module = "用户", action = "更新用户密码")
    public Boolean updateUserPassword(String newPassword) {

        String userCode = UserContext.getUserCode();
        String encryptedPassword = passwordEncoder.encode(newPassword);

        LambdaUpdateWrapper<UserDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserDO::getUserCode, userCode)
                .set(UserDO::getPassword, encryptedPassword);

        int result = userMapper.update(null, wrapper);

        return result == 1;
    }


    @SneakyThrows
    @Override
    public String updateUserAvatar(MultipartFile file, String userCode) {

        String avatarUrl = fileStorageService.upload(file, MinioConstant.BUCKET_AVATARS);

        LambdaUpdateWrapper<UserDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserDO::getUserCode, userCode)
                .set(UserDO::getAvatarUrl, avatarUrl);

        int success = userMapper.update(null, wrapper);

        if (success > 0) {
            return avatarUrl;
        } else {
            throw new Exception("更新用户头像失败");
        }
    }

    @Override
    public UserInfoVO getUserInfoByCode() {
        Long userId = UserContext.getUserId();

        UserDO userDO = userMapper.selectById(userId);
        Integer role = userDO.getRoleId();
        String roleName = roleMapper.selectById(role).getRoleName();
        UserInfoVO userInfoVO = userConvert.UserDOToUserVO(userDO);
        userInfoVO.setRole(roleName);
        userInfoVO.setRawPhone(userDO.getPhone());

        String avatarUrl = userDO.getAvatarUrl();
        String avatarResultUrl = fileStorageService.getUrl(MinioConstant.BUCKET_AVATARS, avatarUrl, MinioConstant.EXPIRY_MAX_TIME);
        userInfoVO.setAvatarUrl(avatarResultUrl);

        return userInfoVO;
    }

}
