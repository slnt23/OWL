package xyz.nanian.owl.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.nanian.owl.common.exception.BizException;
import xyz.nanian.owl.infra.minio.constant.MinioConstant;
import xyz.nanian.owl.infra.minio.service.FileStorageService;
import xyz.nanian.owl.log.annotation.OperationLog;
import xyz.nanian.owl.log.constant.LogType;
import xyz.nanian.owl.common.result.ResultStatus;
import xyz.nanian.owl.common.security.LoginFailureException;
import xyz.nanian.owl.common.security.TokenRevocationService;
import xyz.nanian.owl.common.utils.regex.RegexUtil;
import xyz.nanian.owl.user.constant.UserConstant;
import xyz.nanian.owl.user.domain.dto.EmailBindDTO;
import xyz.nanian.owl.user.domain.dto.PasswordUpdateDTO;
// [TO_BE_DELETED] import xyz.nanian.owl.user.domain.dto.UserInfoDTO;
import xyz.nanian.owl.user.domain.dto.UserInfoUpdateDTO;
import xyz.nanian.owl.user.domain.entity.UserDO;
import xyz.nanian.owl.user.domain.vo.UserInfoVO;
import xyz.nanian.owl.user.mapper.UserMapper;
import xyz.nanian.owl.user.mapstruct.UserConvert;
import xyz.nanian.owl.user.service.UserService;
import xyz.nanian.owl.common.security.CurrentUserContext;
import xyz.nanian.owl.user.utils.CodeCacheUtil;
import xyz.nanian.owl.user.utils.PasswordPolicy;

import java.util.Objects;
import java.util.Set;
import java.util.List;

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
    private final CodeCacheUtil codeCacheUtil;
    private final TokenRevocationService tokenRevocationService;

    // [TO_BE_DELETED] 旧资料更新接口，请使用 updateUserInfo(UserInfoUpdateDTO)。
    // @Override
    // @Deprecated
    // @OperationLog(type = LogType.USER, module = "用户", action = "更新用户信息", persist = true)
    // public Boolean updateUserInfo(UserInfoDTO userInfoDTO) {
    //     String userCode = CurrentUserContext.getUserCode();
    //     LambdaUpdateWrapper<UserDO> wrapper = new LambdaUpdateWrapper<>();
    //     wrapper.eq(UserDO::getUserCode, userCode);
    //     if (userInfoDTO.getUserName() != null) {
    //         wrapper.set(UserDO::getUserName, userInfoDTO.getUserName());
    //     }
    //     if (userInfoDTO.getEmail() != null) {
    //         wrapper.set(UserDO::getEmail, userInfoDTO.getEmail());
    //     }
    //     if (userInfoDTO.getPhone() != null) {
    //         wrapper.set(UserDO::getPhone, userInfoDTO.getPhone());
    //     }
    //     if (userInfoDTO.getNickname() != null) {
    //         wrapper.set(UserDO::getNickname, userInfoDTO.getNickname());
    //     }
    //     if (userInfoDTO.getRemark() != null) {
    //         wrapper.set(UserDO::getRemark, userInfoDTO.getRemark());
    //     }
    //     int result = userMapper.update(null, wrapper);
    //     return result > 0;
    // }

    // [TO_BE_DELETED] 旧改密接口，请使用 updateUserPassword(PasswordUpdateDTO)。
    // @Override
    // @Deprecated
    // @OperationLog(type = LogType.USER, module = "用户", action = "更新用户密码", persist = true)
    // public Boolean updateUserPassword(String newPassword) {
    //     String userCode = CurrentUserContext.getUserCode();
    //     String encryptedPassword = passwordEncoder.encode(newPassword);
    //     LambdaUpdateWrapper<UserDO> wrapper = new LambdaUpdateWrapper<>();
    //     wrapper.eq(UserDO::getUserCode, userCode)
    //             .set(UserDO::getPassword, encryptedPassword);
    //     int result = userMapper.update(null, wrapper);
    //     return result == 1;
    // }

    /**
     * [UPGRADE] 更新用户资料，仅允许 userName/nickname/phone/remark。
     */
    @Override
    @OperationLog(type = LogType.USER, module = "用户", action = "更新用户资料", persist = true)
    public Boolean updateUserInfo(UserInfoUpdateDTO userInfoUpdateDTO) {
        String userCode = CurrentUserContext.getUserCode();

        LambdaUpdateWrapper<UserDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserDO::getUserCode, userCode);

        if (userInfoUpdateDTO.getUserName() != null) {
            if (userInfoUpdateDTO.getUserName().isBlank()) {
                throw new BizException(ResultStatus.PARAMS_INVALID);
            }
            if (existsByUserName(userInfoUpdateDTO.getUserName(), CurrentUserContext.getUserId())) {
                throw new BizException(ResultStatus.DATA_ALREADY_EXIST);
            }
            wrapper.set(UserDO::getUserName, userInfoUpdateDTO.getUserName());
        }
        if (userInfoUpdateDTO.getNickname() != null) {
            wrapper.set(UserDO::getNickname, userInfoUpdateDTO.getNickname());
        }
        if (userInfoUpdateDTO.getPhone() != null) {
            if (userInfoUpdateDTO.getPhone().isBlank() || !RegexUtil.isPhone(userInfoUpdateDTO.getPhone())) {
                throw new BizException(ResultStatus.PARAMS_INVALID);
            }
            if (existsByPhone(userInfoUpdateDTO.getPhone(), CurrentUserContext.getUserId())) {
                throw new BizException(ResultStatus.DATA_ALREADY_EXIST);
            }
            wrapper.set(UserDO::getPhone, userInfoUpdateDTO.getPhone());
        }
        if (userInfoUpdateDTO.getRemark() != null) {
            wrapper.set(UserDO::getRemark, userInfoUpdateDTO.getRemark());
        }

        return userMapper.update(null, wrapper) > 0;
    }

    /**
     * [UPGRADE] 换绑邮箱，需要新邮箱验证码，成功后旧 token 失效。
     */
    @Override
    @OperationLog(type = LogType.USER, module = "用户", action = "换绑邮箱", persist = true)
    public Boolean updateUserEmail(EmailBindDTO emailBindDTO) {
        Long userId = CurrentUserContext.getUserId();
        UserDO userDO = userMapper.selectById(userId);
        if (userDO == null) {
            throw new LoginFailureException(ResultStatus.NOT_FOUND);
        }
        if (Objects.equals(userDO.getEmail(), emailBindDTO.getNewEmail())) {
            return true;
        }
        if (existsByEmail(emailBindDTO.getNewEmail(), userId)) {
            throw new BizException(ResultStatus.EMAIL_ALREADY_BOUND);
        }

        codeCacheUtil.verifyOrThrow(emailBindDTO.getNewEmail(), emailBindDTO.getCode());

        LambdaUpdateWrapper<UserDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserDO::getId, userId).set(UserDO::getEmail, emailBindDTO.getNewEmail());
        boolean updated = userMapper.update(null, wrapper) > 0;
        if (updated) {
            tokenRevocationService.bumpVersion(userId);
        }
        return updated;
    }

    /**
     * [UPGRADE] 登录后修改密码；已有密码必须校验旧密码，成功后旧 token 失效。
     */
    @Override
    @OperationLog(type = LogType.USER, module = "用户", action = "修改密码", persist = true)
    public Boolean updateUserPassword(PasswordUpdateDTO passwordUpdateDTO) {
        Long userId = CurrentUserContext.getUserId();
        UserDO userDO = userMapper.selectById(userId);
        if (userDO == null) {
            throw new LoginFailureException(ResultStatus.NOT_FOUND);
        }
        if (userDO.getPassword() != null
                && (passwordUpdateDTO.getOldPassword() == null
                || !passwordEncoder.matches(passwordUpdateDTO.getOldPassword(), userDO.getPassword()))) {
            throw new BizException(ResultStatus.OLD_PASSWORD_ERROR);
        }
        if (!PasswordPolicy.isValid(passwordUpdateDTO.getNewPassword())) {
            throw new BizException(ResultStatus.PARAMS_INVALID);
        }

        LambdaUpdateWrapper<UserDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserDO::getId, userId)
                .set(UserDO::getPassword, passwordEncoder.encode(passwordUpdateDTO.getNewPassword()));
        boolean updated = userMapper.update(null, wrapper) > 0;
        if (updated) {
            tokenRevocationService.bumpVersion(userId);
        }
        return updated;
    }

    private boolean existsByUserName(String userName, Long excludeId) {
        LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserDO::getUserName, userName).ne(UserDO::getId, excludeId);
        return userMapper.selectCount(wrapper) > 0;
    }

    private boolean existsByPhone(String phone, Long excludeId) {
        LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserDO::getPhone, phone).ne(UserDO::getId, excludeId);
        return userMapper.selectCount(wrapper) > 0;
    }

    private boolean existsByEmail(String email, Long excludeId) {
        LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserDO::getEmail, email).ne(UserDO::getId, excludeId);
        return userMapper.selectCount(wrapper) > 0;
    }

    @SneakyThrows
    @Override
    @OperationLog(type = LogType.USER, module = "用户", action = "更新用户头像", persist = true)
    public String updateUserAvatar(MultipartFile file, String userCode) {

        validateAvatar(file);
        UserDO userDO = userMapper.selectOne(Wrappers.<UserDO>lambdaQuery().eq(UserDO::getUserCode, userCode));
        String avatarUrl = fileStorageService.upload(file, MinioConstant.BUCKET_AVATARS);

        LambdaUpdateWrapper<UserDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserDO::getUserCode, userCode)
                .set(UserDO::getAvatarUrl, avatarUrl);

        int success = userMapper.update(null, wrapper);

        if (success > 0) {
            if (userDO != null
                    && userDO.getAvatarUrl() != null
                    && !UserConstant.DEFAULT_AVATAR.equals(userDO.getAvatarUrl())) {
                fileStorageService.delete(MinioConstant.BUCKET_AVATARS, userDO.getAvatarUrl());
            }
            return avatarUrl;
        } else {
            throw new BizException(ResultStatus.BIZ_ERROR);
        }
    }

    @Override
    public UserInfoVO getUserInfoByCode() {
        Long userId = CurrentUserContext.getUserId();

        UserDO userDO = userMapper.selectById(userId);
        if (userDO == null) {
            throw new LoginFailureException(ResultStatus.NOT_FOUND);
        }
        if (!Objects.equals(userDO.getStatus(), UserConstant.DEFAULT_STATUS)) {
            throw new LoginFailureException(ResultStatus.ACCOUNT_DISABLED);
        }
        String roleName = userDO.getRoleName();
        UserInfoVO userInfoVO = userConvert.UserDOToUserVO(userDO);
        userInfoVO.setRole(roleName);
        // [TO_BE_DELETED] rawPhone 字段废弃，不再回填

        String avatarUrl = userDO.getAvatarUrl();
        String avatarResultUrl = fileStorageService.getUrl(MinioConstant.BUCKET_AVATARS, avatarUrl, MinioConstant.EXPIRY_MAX_TIME);
        userInfoVO.setAvatarUrl(avatarResultUrl);

        return userInfoVO;
    }

    private void validateAvatar(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BizException(ResultStatus.FILE_TYPE_ERROR);
        }
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new BizException(ResultStatus.FILE_SIZE_EXCEEDED);
        }
        Set<String> allowedTypes = Set.of("image/jpeg", "image/png", "image/webp");
        if (!allowedTypes.contains(file.getContentType())) {
            throw new BizException(ResultStatus.FILE_TYPE_ERROR);
        }
    }

}
