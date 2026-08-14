package xyz.nanian.owl.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.common.result.ResultStatus;
import xyz.nanian.owl.common.security.LoginFailureException;
import xyz.nanian.owl.common.security.TokenRevocationService;
import xyz.nanian.owl.common.mail.MailService;
import xyz.nanian.owl.common.utils.regex.RegexUtil;
import xyz.nanian.owl.api.domain.entity.RoleDO;
import xyz.nanian.owl.api.mapper.RoleMapper;
import xyz.nanian.owl.common.result.Result;
// [TO_BE_DELETED] import xyz.nanian.owl.user.domain.dto.EmailLoginOrRegisterDTO;
import xyz.nanian.owl.user.domain.dto.EmailLoginDTO;
import xyz.nanian.owl.user.domain.dto.PasswordLoginDTO;
import xyz.nanian.owl.user.domain.dto.ResetPasswordDTO;
import xyz.nanian.owl.user.domain.dto.SendCodeDTO;
import xyz.nanian.owl.user.domain.entity.UserDO;
import xyz.nanian.owl.user.mapper.UserMapper;
import xyz.nanian.owl.user.service.LoginService;

import xyz.nanian.owl.user.constant.UserConstant;
import xyz.nanian.owl.user.constant.LoginConstant;
import xyz.nanian.owl.common.security.JwtTokenProvider;
import xyz.nanian.owl.user.utils.CodeCacheUtil;
import xyz.nanian.owl.user.utils.PasswordPolicy;

import java.time.LocalDateTime;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * login，register
 *
 * @author slnt23
 * @since 2026/4/9
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    final MailService mailService;
    final UserMapper userMapper;
    final StringRedisTemplate stringRedisTemplate;
    final PasswordEncoder passwordEncoder;
    final CodeCacheUtil codeCacheUtil;
    private final RoleMapper roleMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRevocationService tokenRevocationService;

    /**
     * 发送验证码
     *
     * @param sendCodeDTO
     * @return
     */
    @Override
    public Result<String> sendVerificationCode(SendCodeDTO sendCodeDTO) {

        String emailAddress = sendCodeDTO.getEmail();

        // [UPGRADE] 先校验邮箱，再走 5 分钟冷却
        if (Objects.isNull(emailAddress) || !RegexUtil.isEmail(emailAddress)) {
            return Result.fail();
        }

        if (codeCacheUtil.isLocked(emailAddress)) {
            return Result.fail(ResultStatus.CODE_SEND_TOO_FREQUENT);
        }

        // 2. 生成6位随机验证码
        String verificationCode = generateVerificationCode();

        // 3. 构建邮件内容
        String subject = "「OWL 账号验证」";
        String body = String.format("""
                尊敬的用户：
                
                您好！
                
                您的验证码为：**%s**
                
                该验证码有效期为 %d 分钟，请在有效时间内使用。
                为保障您的账户安全，请勿将验证码泄露给他人。
                
                如果这不是您本人的操作，请忽略此邮件。
                
                此致
                敬礼
                [Energy] 团队
                """, verificationCode, LoginConstant.CODE_EXPIRE_MINUTES);
        // 4. 发送邮件
        mailService.send(emailAddress, subject, body);

        // 5. 保存验证码到Redis
        String redisKey = LoginConstant.VERIFICATION_CODE_PREFIX + emailAddress;
        stringRedisTemplate
                .opsForValue()
                .set(redisKey, verificationCode, LoginConstant.CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);

        // 5.1 新验证码发送成功后重置错误次数
        stringRedisTemplate.delete(LoginConstant.CODE_ATTEMPT_PREFIX + emailAddress);

        return Result.success();
    }

    // [TO_BE_DELETED] 旧注册接口，已由邮箱登录自动注册取代。
    // @Override
    // @Deprecated
    // public String saveUser(EmailLoginOrRegisterDTO emailLoginOrRegisterDTO) {
    //     EmailLoginDTO emailLoginDTO = new EmailLoginDTO();
    //     emailLoginDTO.setEmail(emailLoginOrRegisterDTO.getEmail());
    //     emailLoginDTO.setCode(emailLoginOrRegisterDTO.getCode());
    //     return login(emailLoginDTO);
    // }

    // [TO_BE_DELETED] 旧邮箱验证码登录，已升级为 login(EmailLoginDTO)。
    // @Override
    // @Deprecated
    // public String login(EmailLoginOrRegisterDTO emailLoginOrRegisterDTO) {
    //     EmailLoginDTO emailLoginDTO = new EmailLoginDTO();
    //     emailLoginDTO.setEmail(emailLoginOrRegisterDTO.getEmail());
    //     emailLoginDTO.setCode(emailLoginOrRegisterDTO.getCode());
    //     return login(emailLoginDTO);
    // }

    /**
     * [UPGRADE] 密码登录，role 从数据库读取，补齐账号状态与角色启用校验。
     */
    @Override
    public String login(PasswordLoginDTO passwordLoginDTO) {
        UserDO userDO = findByEmail(passwordLoginDTO.getEmail());
        if (Objects.isNull(userDO)) {
            throw new LoginFailureException(ResultStatus.NOT_FOUND);
        }
        if (!Objects.equals(userDO.getStatus(), UserConstant.DEFAULT_STATUS)) {
            throw new LoginFailureException(ResultStatus.ACCOUNT_DISABLED);
        }
        if (userDO.getPassword() == null) {
            throw new LoginFailureException(ResultStatus.PASSWORD_NO_REWRITE);
        }
        if (!passwordEncoder.matches(passwordLoginDTO.getPassword(), userDO.getPassword())) {
            throw new LoginFailureException(ResultStatus.PARAMS_INVALID);
        }
        RoleDO roleDO = loadEnabledRole(userDO);

        return getToken(userDO, roleDO);
    }

    /**
     * [UPGRADE] 邮箱验证码登录，未注册邮箱自动创建默认 USER 账号。
     */
    @Override
    public String login(EmailLoginDTO emailLoginDTO) {
        verifyEmailCode(emailLoginDTO.getEmail(), emailLoginDTO.getCode());

        UserDO userDO = findByEmail(emailLoginDTO.getEmail());
        if (Objects.isNull(userDO)) {
            userDO = createOrGetUser(emailLoginDTO.getEmail());
        }
        if (!Objects.equals(userDO.getStatus(), UserConstant.DEFAULT_STATUS)) {
            throw new LoginFailureException(ResultStatus.ACCOUNT_DISABLED);
        }
        RoleDO roleDO = loadEnabledRole(userDO);

        return getToken(userDO, roleDO);
    }

    /**
     * [UPGRADE] 忘记密码重置，重置后旧 token 全部失效。
     */
    @Override
    public void resetPassword(ResetPasswordDTO resetPasswordDTO) {
        verifyEmailCode(resetPasswordDTO.getEmail(), resetPasswordDTO.getCode());

        UserDO userDO = findByEmail(resetPasswordDTO.getEmail());
        if (Objects.isNull(userDO)) {
            throw new LoginFailureException(ResultStatus.NOT_FOUND);
        }
        if (!PasswordPolicy.isValid(resetPasswordDTO.getNewPassword())) {
            throw new LoginFailureException(ResultStatus.PARAMS_INVALID);
        }

        LambdaUpdateWrapper<UserDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserDO::getEmail, resetPasswordDTO.getEmail())
                .set(UserDO::getPassword, passwordEncoder.encode(resetPasswordDTO.getNewPassword()));
        userMapper.update(null, wrapper);

        tokenRevocationService.bumpVersion(userDO.getId());
    }

    /**
     * [UPGRADE] 登出，当前 token 加入黑名单直到自然过期。
     */
    @Override
    public void logout(String token) {
        if (token == null || token.isBlank()) {
            return;
        }
        io.jsonwebtoken.Claims claims = jwtTokenProvider.parseToken(token);
        String jti = claims.getId();
        if (jti == null || claims.getExpiration() == null) {
            return;
        }
        long ttlSeconds = Math.max(1,
                (claims.getExpiration().getTime() - System.currentTimeMillis()) / 1000);
        tokenRevocationService.revoke(jti, ttlSeconds);
    }


    // [TO_BE_DELETED] 旧 token 生成逻辑，仅保留给旧代码审查。
    // @Deprecated
    // private String getToken(String email) {
    //     LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery();
    //     wrapper.eq(UserDO::getEmail, email);
    //     UserDO userDO = userMapper.selectOne(wrapper);
    //     String userCode = userDO.getUserCode();
    //     Long userId = userDO.getId();
    //     RoleDO roleDO = roleMapper.selectById(userDO.getRoleId());
    //     return jwtTokenProvider.generateToken(userId, userCode, email, roleDO.getRoleName());
    // }

    /**
     * [UPGRADE] 使用 jti 和 tokenVersion 生成 token。
     */
    private String getToken(UserDO userDO, RoleDO roleDO) {
        long tokenVersion = tokenRevocationService.getTokenVersion(userDO.getId());
        return jwtTokenProvider.generateToken(
                userDO.getId(),
                userDO.getUserCode(),
                userDO.getEmail(),
                roleDO.getRoleName(),
                tokenVersion);
    }

    private UserDO findByEmail(String email) {
        LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserDO::getEmail, email);
        return userMapper.selectOne(wrapper);
    }

    private RoleDO loadEnabledRole(UserDO userDO) {
        RoleDO roleDO = roleMapper.selectById(userDO.getRoleId());
        if (roleDO == null || !Boolean.TRUE.equals(roleDO.getEnabled())) {
            throw new LoginFailureException(ResultStatus.ROLE_FAILED);
        }
        return roleDO;
    }

    private UserDO createUser(String email) {
        UserDO userDO = new UserDO();
        String uuid = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        userDO.setUserCode(uuid);
        userDO.setUserName(UserConstant.DEFAULT_USER_NAME + uuid);
        userDO.setPassword(null);
        userDO.setEmail(email);
        userDO.setAvatarUrl(UserConstant.DEFAULT_AVATAR);
        userDO.setRoleId(UserConstant.DEFAULT_ROLE);
        userDO.setStatus(UserConstant.DEFAULT_STATUS);
        userDO.setNickname(UserConstant.DEFAULT_NICK_NAME);
        userDO.setRemark(UserConstant.DEFAULT_REMARK);
        userDO.setCreateTime(now);

        userMapper.insert(userDO);
        return userDO;
    }

    /**
     * 并发首次注册时，数据库唯一索引只会允许一个请求插入成功，
     * 另一个请求回查已有用户后继续登录。
     */
    private UserDO createOrGetUser(String email) {
        try {
            return createUser(email);
        } catch (DuplicateKeyException e) {
            UserDO existing = findByEmail(email);
            if (existing == null) {
                throw e;
            }
            return existing;
        }
    }

    private void verifyEmailCode(String email, String code) {
        codeCacheUtil.verifyOrThrow(email, code);
    }

    /**
     * 生成6位数字验证码
     */
    private String generateVerificationCode() {
        int code = 100000 + SECURE_RANDOM.nextInt(900000);
        return String.valueOf(code);
    }

    // [TO_BE_DELETED] 旧验证码校验逻辑，已迁移到 CodeCacheUtil。
    // private Boolean verificationCode(String email, String code) {
    //     String key = LoginConstant.VERIFICATION_CODE_PREFIX + email;
    //     String verificationCode = stringRedisTemplate.opsForValue().get(key);
    //     if (!Objects.equals(verificationCode, code)) {
    //         return false;
    //     }
    //     stringRedisTemplate.delete(key);
    //     return true;
    // }

    // [TO_BE_DELETED] 旧建号逻辑，已由 createUser 取代。
    // private Boolean saveUserInfo(String email) {
    //     UserDO userDO = new UserDO();
    //     String uuid = UUID.randomUUID().toString();
    //     LocalDateTime now = LocalDateTime.now();
    //     userDO.setUserCode(uuid);
    //     userDO.setUserName(UserConstant.DEFAULT_USER_NAME + uuid);
    //     userDO.setPassword(null);
    //     userDO.setEmail(email);
    //     userDO.setAvatarUrl(UserConstant.DEFAULT_AVATAR);
    //     userDO.setRoleId(UserConstant.DEFAULT_ROLE);
    //     userDO.setStatus(UserConstant.DEFAULT_STATUS);
    //     userDO.setNickname(UserConstant.DEFAULT_NICK_NAME);
    //     userDO.setRemark(UserConstant.DEFAULT_REMARK);
    //     userDO.setCreateTime(now);
    //     userMapper.insert(userDO);
    //     return true;
    // }

}
