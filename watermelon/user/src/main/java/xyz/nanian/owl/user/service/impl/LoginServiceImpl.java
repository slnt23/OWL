package xyz.nanian.owl.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.common.result.ResultStatus;
import xyz.nanian.owl.common.security.LoginFailureException;
import xyz.nanian.owl.user.domain.entity.RoleDO;
import xyz.nanian.owl.user.mapper.RoleMapper;
import xyz.nanian.owl.user.utils.MailUtil;
import xyz.nanian.owl.common.result.Result;
import xyz.nanian.owl.user.domain.dto.EmailLoginOrRegisterDTO;
import xyz.nanian.owl.user.domain.dto.PasswordLoginDTO;
import xyz.nanian.owl.user.domain.dto.SendCodeDTO;
import xyz.nanian.owl.user.domain.entity.UserDO;
import xyz.nanian.owl.user.mapper.UserMapper;
import xyz.nanian.owl.user.service.LoginService;

import xyz.nanian.owl.user.constant.UserConstant;
import xyz.nanian.owl.user.constant.LoginConstant;
import xyz.nanian.owl.common.security.JwtTokenProvider;
import xyz.nanian.owl.user.utils.CodeCacheUtil;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;
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

    final MailUtil mailUtil;
    final UserMapper userMapper;
    final StringRedisTemplate stringRedisTemplate;
    final PasswordEncoder passwordEncoder;
    final CodeCacheUtil codeCacheUtil;
    private final RoleMapper roleMapper;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 发送验证码
     *
     * @param sendCodeDTO
     * @return
     */
    @Override
    public Result<String> sendVerificationCode(SendCodeDTO sendCodeDTO) {

        String emailAddress = sendCodeDTO.getEmail();

//        这里加一步，5分钟内不可重复发，
        if (codeCacheUtil.isLocked(emailAddress)) {
            return Result.fail(LoginConstant.CODE_TIME_IN_5_MIN);
        }

        if (Objects.isNull(emailAddress)) {
            return Result.fail();
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
        mailUtil.sendMail(emailAddress, subject, body);

        // 5. 保存验证码到Redis
        String redisKey = LoginConstant.VERIFICATION_CODE_PREFIX + emailAddress;
        stringRedisTemplate
                .opsForValue()
                .set(redisKey, verificationCode, LoginConstant.CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);

        return Result.success();
    }

    /**
     * 保存用户信息，可以用于注册
     * TODO(login): 注册与登录流程合并后可删除本方法
     *
     * @param emailLoginOrRegisterDTO 用户DTO基本信息
     * @return 登录 Token
     */
    @Override
    public String saveUser(EmailLoginOrRegisterDTO emailLoginOrRegisterDTO) {
//        1. 检验验证码是否正确,正确生成用户，错误，返回
        if (!verificationCode(emailLoginOrRegisterDTO.getEmail(), emailLoginOrRegisterDTO.getCode())) {
            throw new LoginFailureException(ResultStatus.VERIFY_CODE_ERROR);
        }

//        生成用户信息，
        if (!saveUserInfo(emailLoginOrRegisterDTO.getEmail())) {
            throw new LoginFailureException(ResultStatus.BIZ_ERROR);
        }

        return getToken(emailLoginOrRegisterDTO.getEmail());
    }

    /**
     * 用户登陆 + 注册 ，邮箱验证码，
     *
     * @param emailLoginOrRegisterDTO DTO
     * @return
     */
    @Override
    public String login(EmailLoginOrRegisterDTO emailLoginOrRegisterDTO) {

//        1. 检验验证码是否正确
        if (!verificationCode(emailLoginOrRegisterDTO.getEmail(), emailLoginOrRegisterDTO.getCode())) {
            throw new LoginFailureException(ResultStatus.VERIFY_CODE_ERROR);
        }

//        3.检查用户账号是否封禁，0 = 正常
        LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserDO::getEmail, emailLoginOrRegisterDTO.getEmail());
        UserDO userDO = userMapper.selectOne(wrapper);
        if (userDO.getStatus() != 0) {
            throw new LoginFailureException(ResultStatus.ACCOUNT_DISABLED);
        }

//        4.查询用户role是否匹配，
        String role = emailLoginOrRegisterDTO.getRole();
        RoleDO roleDO = roleMapper.selectById(userDO.getRoleId());
        if (!roleDO.getRoleName().equals(role)) {
            throw new LoginFailureException(ResultStatus.ROLE_FAILED);
        }

//        5.一切成功,
        return getToken(emailLoginOrRegisterDTO.getEmail());
    }

    /**
     * 用户登陆，密码
     *
     * @param passwordLoginDTO
     * @return
     */
    @Override
    public String login(PasswordLoginDTO passwordLoginDTO) {

//        1. 数据库中获取密码，比对经过加密后的密码，
        LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserDO::getEmail, passwordLoginDTO.getEmail());

        UserDO userDO = userMapper.selectOne(wrapper);
        if (Objects.isNull(userDO)) {
            throw new LoginFailureException(ResultStatus.NOT_FOUND);
        }

//        2.查询用户role
        String role = passwordLoginDTO.getRole();
        RoleDO roleDO = roleMapper.selectById(userDO.getRoleId());

//        3. 比对，判断用户
        if (userDO.getStatus() == 1) {
            throw new LoginFailureException(ResultStatus.ACCOUNT_DISABLED);
        } else if (userDO.getPassword() == null) {
            throw new LoginFailureException(ResultStatus.PASSWORD_NO_REWRITE);
        } else if (!passwordEncoder.matches(passwordLoginDTO.getPassword(), userDO.getPassword())) {
            throw new LoginFailureException(ResultStatus.PARAMS_INVALID);
        } else if (!roleDO.getRoleName().equals(role)) {
            throw new LoginFailureException(ResultStatus.ROLE_FAILED);
        }

//        返回token
        return getToken(passwordLoginDTO.getEmail());
    }


    /**
     * 获取token，并在token中注入用户信息，
     *
     * @return
     */
    private String getToken(String email) {

        LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserDO::getEmail, email);

        UserDO userDO = userMapper.selectOne(wrapper);

        String userCode = userDO.getUserCode();
        Long userId = userDO.getId();

        RoleDO roleDO = roleMapper.selectById(userDO.getRoleId());
        return jwtTokenProvider.generateToken(userId, userCode, email, roleDO.getRoleName());
    }

    /**
     * 生成6位数字验证码
     */
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    /**
     * 检验验证码是否正确，
     *
     * @param email
     * @param code
     * @return
     */
    private Boolean verificationCode(String email, String code) {
//        1.根据获取的邮箱地址，以及邮箱KEY 获取redis中的code，
        String key = LoginConstant.VERIFICATION_CODE_PREFIX + email;

        String verificationCode = stringRedisTemplate
                .opsForValue()
                .get(key);

//        2. 比对code，
        if (!Objects.equals(verificationCode, code)) {
            return false;
        }
//        3. 删除验证码，防止成为短期密码，无限使用，
        stringRedisTemplate.delete(key);
        return true;
    }

    /**
     * 生成默认用户信息，并保存
     *
     * @param email
     * @return
     */
    private Boolean saveUserInfo(String email) {

//        2. 生成用户信息并注入默认值，
        UserDO userDO = new UserDO();
        String uuid = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        userDO.setUserCode(uuid);
        userDO.setUserName(UserConstant.DEFAULT_USER_NAME + uuid);
//        3. 这里默认密码为空，当登陆时检测密码为空则不可进行密码登录，只能够验证码登录，只有用户更改密码后，才可以用密码登陆，
        userDO.setPassword(null);
        userDO.setEmail(email);
        userDO.setAvatarUrl(UserConstant.DEFAULT_AVATAR);
//        默认分配 user 角色，角色变更由后端管理，不允许前端传 role
        userDO.setRoleId(UserConstant.DEFAULT_ROLE);
        userDO.setStatus(UserConstant.DEFAULT_STATUS);
        userDO.setNickname(UserConstant.DEFAULT_NICK_NAME);
        userDO.setRemark(UserConstant.DEFAULT_REMARK);
        userDO.setCreateTime(now);

        userMapper.insert(userDO);
        return true;
    }

}
