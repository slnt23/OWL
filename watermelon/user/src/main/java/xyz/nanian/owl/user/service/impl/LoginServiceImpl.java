package xyz.nanian.owl.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.result.ResultStatus;
import xyz.nanian.owl.exception.LoginException;
import xyz.nanian.owl.user.domain.entity.RoleDO;
import xyz.nanian.owl.user.mapper.RoleMapper;
import xyz.nanian.owl.utils.mail.MailUtil;
import xyz.nanian.owl.result.Result;
import xyz.nanian.owl.user.domain.dto.EmailLoginOrRegisterDTO;
import xyz.nanian.owl.user.domain.dto.PasswordLoginDTO;
import xyz.nanian.owl.user.domain.dto.SendCodeDTO;
import xyz.nanian.owl.user.domain.entity.UserDO;
import xyz.nanian.owl.user.mapper.UserMapper;
import xyz.nanian.owl.user.service.LoginService;

import xyz.nanian.owl.user.constant.UserConstant;
import xyz.nanian.owl.constant.LoginConstant;
import xyz.nanian.owl.utils.jwt.JwtUtil;
import xyz.nanian.owl.infrastructure.redis.util.CodeCacheUtil;

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

    //    这里这个类的注册问题尚未解决，//已解决，是注入方式的问题，
    final MailUtil mailUtil;
    final UserMapper userMapper;
    final StringRedisTemplate stringRedisTemplate;
    final PasswordEncoder passwordEncoder;
    final CodeCacheUtil codeCacheUtil;
    private final RoleMapper roleMapper;

    /**
     * 发送验证码
     *
     * @param sendCodeDTO
     * @return
     */
    @Override
    public Result<String> sendVerificationCode(SendCodeDTO sendCodeDTO) {

        String emailAddress = sendCodeDTO.getEmail();
//                "1693676136@qq.com";

//        这里加一步，5分钟内不可重复发，
        if (codeCacheUtil.isLocked(emailAddress)) {
            return Result.fail(LoginConstant.CODE_TIME_IN_5_MIN);
        }

        if (Objects.isNull(emailAddress)) {
            return Result.fail();
        }

        // 1. 检查邮箱是否已注册（可选）

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

        // 6. 记录日志（可选）后续添加，

        return Result.success();
    }

    /**
     * 保存用户信息，可以用于注册,TODO 当前端注册，登陆合一，本方法可删除，
     *
     * @param emailLoginOrRegisterDTO 用户DTO基本信息
     * @return
     */
    @Override
    public String saveUser(EmailLoginOrRegisterDTO emailLoginOrRegisterDTO) {
//        1. 检验验证码是否正确,正确生成用户，错误，返回
        if (!verificationCode(emailLoginOrRegisterDTO.getEmail(), emailLoginOrRegisterDTO.getCode())) {
            throw new LoginException(ResultStatus.VERIFY_CODE_ERROR);
        }

//        2. 生成用户信息并注入默认值，
//        UserDO userDO = new UserDO();
//        String uuid = UUID.randomUUID().toString();
//        LocalDateTime now = LocalDateTime.now();
////        String password = passwordEncoder.encode(UserConstant.DEFAULT_PASSWORD);
//
//        userDO.setUserCode(uuid);
//        userDO.setUserName(UserConstant.DEFAULT_USER_NAME + uuid);
////        初始密码都是加密后的的”123456“，后续用户更改密码，也设定加密
////        3. 这里默认密码为空，当登陆时检测密码为空则不可进行密码登录，只能够验证码登录，只有用户更改密码后，才可以用密码登陆，
////        userDO.setPassword(password);
//        userDO.setPassword(null);
//        userDO.setEmail(emailLoginOrRegisterDTO.getEmail());
//        userDO.setAvatarUrl(UserConstant.DEFAULT_AVATAR);
////        这里后续可以改为搜索角色，再填入相关的id,目前可以默认 0=user,
////        也可以不，防止前端随意传 role 信息，后端统一设计 user ，可以在管理端设计一个更改 role 的，让用户申请，
//        userDO.setRoleId(UserConstant.DEFAULT_ROLE);
//        userDO.setStatus(UserConstant.DEFAULT_STATUS);
//        userDO.setNickName(UserConstant.DEFAULT_NICK_NAME);
//        userDO.setRemark(UserConstant.DEFAULT_REMARK);
//        userDO.setCreateTime(now);
//
//        userMapper.insert(userDO);

//        生成用户信息，
        if (!saveUserInfo(emailLoginOrRegisterDTO.getEmail())) {
            throw new LoginException(ResultStatus.BIZ_ERROR);
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

//        1.根据获取的邮箱地址，以及邮箱KEY 获取redis中的code，
//        String key = LoginConstant.VERIFICATION_CODE_PREFIX + emailLoginOrRegisterDTO.getEmail();
//
//        String verificationCode = stringRedisTemplate
//                .opsForValue()
//                .get(key);
//
//        2. 比对code，然后如果正确，登陆1天
//        if (!Objects.equals(verificationCode, emailLoginOrRegisterDTO.getCode())) {
//            throw new LoginException(ResultStatus.FAIL);
//        }
//        删除验证码，防止成为短期密码，无限使用，
//        stringRedisTemplate.delete(key);

//        1. 检验验证码是否正确
        if (!verificationCode(emailLoginOrRegisterDTO.getEmail(), emailLoginOrRegisterDTO.getCode())) {
            throw new LoginException(ResultStatus.VERIFY_CODE_ERROR);
        }

//        2.  搜索数据库是否有此账户，有直接登陆，无注册用户，
//        TODO 后期可以设置为验证码登陆，注册二合一，也就是将上面的方法与本方法融合，
//        if (!saveUserInfo(emailLoginOrRegisterDTO.getEmail())) {
//            throw new LoginException(ResultStatus.BIZ_ERROR);
//        }

//        3.检查用户账号是否封禁，0 = 正常
        LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserDO::getEmail, emailLoginOrRegisterDTO.getEmail());
        UserDO userDO = userMapper.selectOne(wrapper);
        if (userDO.getStatus() != 0) {
            throw new LoginException(ResultStatus.ACCOUNT_DISABLED);
        }

//        4.查询用户role是否匹配，
        String role = emailLoginOrRegisterDTO.getRole();
        RoleDO roleDO = roleMapper.selectById(userDO.getRoleId());
        if (!roleDO.getRoleName().equals(role)) {
            throw new LoginException(ResultStatus.ROLE_FAILED);
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
            throw new LoginException(ResultStatus.NOT_FOUND);
        }

//        2.查询用户role
        String role = passwordLoginDTO.getRole();
        RoleDO roleDO = roleMapper.selectById(userDO.getRoleId());

//        3. 比对，判断用户
        if (userDO.getStatus() == 1) {
            throw new LoginException(ResultStatus.ACCOUNT_DISABLED);
        } else if (userDO.getPassword() == null) {
            throw new LoginException(ResultStatus.PASSWORD_NO_REWRITE);
        } else if (!passwordEncoder.matches(passwordLoginDTO.getPassword(), userDO.getPassword())) {
            throw new LoginException(ResultStatus.PARAMS_INVALID);
        } else if (!roleDO.getRoleName().equals(role)) {
            throw new LoginException(ResultStatus.ROLE_FAILED);
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

        return JwtUtil.generateToken(userId, userCode, email);
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
//        这里后续可以改为搜索角色，再填入相关的id,目前可以默认 0=user,
//        也可以不，防止前端随意传 role 信息，后端统一设计 user ，可以在管理端设计一个更改 role 的，让用户申请，
        userDO.setRoleId(UserConstant.DEFAULT_ROLE);
        userDO.setStatus(UserConstant.DEFAULT_STATUS);
        userDO.setNickname(UserConstant.DEFAULT_NICK_NAME);
        userDO.setRemark(UserConstant.DEFAULT_REMARK);
        userDO.setCreateTime(now);

        userMapper.insert(userDO);
        return true;
    }

}
