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
import xyz.nanian.owl.utils.mail.MailUtil;
import xyz.nanian.owl.result.Result;
import xyz.nanian.owl.user.domain.dto.EmailLoginOrRegisterDTO;
import xyz.nanian.owl.user.domain.dto.PasswordLoginDTO;
import xyz.nanian.owl.user.domain.dto.SendCodeDTO;
import xyz.nanian.owl.user.domain.entity.UserDO;
import xyz.nanian.owl.user.mapper.UserMapper;
import xyz.nanian.owl.user.service.LoginService;

import xyz.nanian.owl.constant.UserConstant;
import xyz.nanian.owl.constant.LoginConstant;
import xyz.nanian.owl.utils.jwt.JwtUtil;
import xyz.nanian.owl.utils.redis.CodeCacheUtil;

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
    MailUtil mailUtil;
    UserMapper userMapper;
    StringRedisTemplate stringRedisTemplate;
    PasswordEncoder passwordEncoder;
    CodeCacheUtil codeCacheUtil;

    /**
     * 发送验证码
     * @param sendCodeDTO
     * @return
     */
    @Override
    public Result<String> sendVerificationCode(SendCodeDTO sendCodeDTO) {

        String emailAddress = sendCodeDTO.getEmail();
//                "1693676136@qq.com";

        //        这里加一步，5分钟内不可重复发，
        if(codeCacheUtil.isLocked(emailAddress)){
            return Result.fail(LoginConstant.CODE_TIME_IN_5_MIN);
        }

        if (Objects.isNull(emailAddress)) {
            return Result.fail();
        }

        // 1. 检查邮箱是否已注册（可选）

        // 2. 生成6位随机验证码
        String verificationCode = generateVerificationCode();

        // 3. 构建邮件内容
        String subject = "验证码 - 欢迎加入 OWL 兴趣探索";
        String body = String.format("""
                    您的验证码是：%s
                    
                    验证码有效期为 %d 分钟，请勿泄露给他人。
                    
                    如果这不是您本人的操作，请忽略此邮件。
                    """, verificationCode,LoginConstant.CODE_EXPIRE_MINUTES);
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
     * 保存用户信息，
     * 可以用于注册
     *
     * @param emailLoginOrRegisterDTO 用户DTO基本信息
     * @return
     */
    @Override
    public Boolean saveUser(EmailLoginOrRegisterDTO emailLoginOrRegisterDTO) {

//        检验验证码是否正确,正确生成用户，错误，返回

//        生成用户信息并注入默认值，
        UserDO userDO = new UserDO();
        String uuid = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        String password = passwordEncoder.encode(UserConstant.DEFAULT_PASSWORD);

        userDO.setUserCode(uuid);
        userDO.setUserName(UserConstant.DEFAULT_USER_NAME +uuid);
//        非明文密码
//        初始密码都是加密后的的”123456“，后续用户更改密码，也设定加密
        userDO.setPassword(password);
        userDO.setEmail(emailLoginOrRegisterDTO.getEmail());
        userDO.setAvatar(UserConstant.DEFAULT_AVATAR);
        userDO.setRole(UserConstant.DEFAULT_ROLE);
        userDO.setStatus(UserConstant.DEFAULT_STATUS);
        userDO.setCreateTime(now);

        userMapper.insert(userDO);
        return true;
    }

    /**
     * 用户登陆，邮箱验证码，
     * TODO 后期可以设置为验证码登陆，注册二合一，
     * @param emailLoginOrRegisterDTO 手机号
     * @return
     */
    @Override
    public String login(EmailLoginOrRegisterDTO emailLoginOrRegisterDTO) {

//        1.根据获取的邮箱地址，以及邮箱KEY 获取redis中的code，
        String key = LoginConstant.VERIFICATION_CODE_PREFIX + emailLoginOrRegisterDTO.getEmail();

        String verificationCode = stringRedisTemplate
                .opsForValue()
                .get(key);

//        2. 比对code，然后如果正确，登陆30天
        if(!Objects.equals(verificationCode, emailLoginOrRegisterDTO.getCode())){
            throw new LoginException(ResultStatus.FAIL);
        }
//        删除验证码，放置成为短期密码，无限使用，
        stringRedisTemplate.delete(key);

        return getToken(emailLoginOrRegisterDTO.getEmail());
    }

    /**
     * 用户登陆，密码
     * @param passwordLoginDTO
     * @return
     */
    @Override
    public String login(PasswordLoginDTO passwordLoginDTO) {

//        1. 数据库中获取密码，比对经过加密后的密码，
        LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserDO::getEmail, passwordLoginDTO.getEmail());

        UserDO userDO = userMapper.selectOne(wrapper);

//        2. 比对，
//        判断用户
        if(Objects.isNull(userDO)){
//            throw new LoginException(ExceptionConstant.USER_NOT_EXIST);
            throw new LoginException(ResultStatus.NOT_FOUND);
        }

//        判断密码
        if(!passwordEncoder.matches(passwordLoginDTO.getPassword(),userDO.getPassword())){
            throw new LoginException(ResultStatus.FAIL);
//            throw new LoginException(ExceptionConstant.PASSWORD_ERROR);
        }

//        返回token
        return getToken(passwordLoginDTO.getEmail());
    }


    /**
     * 获取token，并在token中注入用户信息，
     * @return
     */
    private String getToken(String email){

        LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserDO::getEmail, email);

        UserDO userDO = userMapper.selectOne(wrapper);

        String userCode= userDO.getUserCode();
        Long userId = userDO.getId();

        return JwtUtil.generateToken(userId,userCode,email);
    }

    /**
     * 生成6位数字验证码
     */
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

}
