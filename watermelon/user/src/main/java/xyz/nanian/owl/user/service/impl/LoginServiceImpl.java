package xyz.nanian.owl.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.mail.Mail;
import xyz.nanian.owl.user.dto.EmailLoginOrRegisterDTO;
import xyz.nanian.owl.user.dto.PasswordLoginDTO;
import xyz.nanian.owl.user.dto.SendCodeDTO;
import xyz.nanian.owl.user.entity.UserDO;
import xyz.nanian.owl.user.mapper.UserMapper;
import xyz.nanian.owl.user.service.LoginService;

import xyz.nanian.owl.user.constant.UserConstant;
import xyz.nanian.owl.user.constant.LoginConstant;

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

@Service
public class LoginServiceImpl implements LoginService {

//    这里这个类的注册问题尚未解决，
    Mail mail;
    UserMapper userMapper;
    StringRedisTemplate stringRedisTemplate;
    PasswordEncoder passwordEncoder;

    public LoginServiceImpl(UserMapper userMapper,
                            StringRedisTemplate stringRedisTemplate,
                            PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.stringRedisTemplate = stringRedisTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 发送验证码
     * @param sendCodeDTO
     * @return
     */
    @Override
    public Boolean sendVerificationCode(SendCodeDTO sendCodeDTO) {

        String emailAddress = sendCodeDTO.getEmail();

        // 1. 检查邮箱是否已注册（可选）

        // 2. 生成6位随机验证码
        String verificationCode = generateVerificationCode();

        // 3. 构建邮件内容
        String subject = "验证码 - 您的验证码";
        String body = String.format("""
                    您的验证码是：%s
                    
                    验证码有效期为 %d 分钟，请勿泄露给他人。
                    
                    如果这不是您本人的操作，请忽略此邮件。
                    """, verificationCode,LoginConstant.CODE_EXPIRE_MINUTES);
        // 4. 发送邮件
        mail.sendMail(emailAddress, subject, body);

        // 5. 保存验证码到Redis
        String redisKey = LoginConstant.VERIFICATION_CODE_PREFIX + emailAddress;
        stringRedisTemplate
                .opsForValue()
                .set(redisKey, verificationCode, LoginConstant.CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);

        // 6. 记录日志（可选）后续添加，

        return true;
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

    @Override
    public Boolean login(EmailLoginOrRegisterDTO emailLoginOrRegisterDTO) {

//        1.根据获取的邮箱地址，以及邮箱KEY 获取redis中的code，
        String key = LoginConstant.VERIFICATION_CODE_PREFIX + emailLoginOrRegisterDTO.getEmail();

        String verificationCode = stringRedisTemplate
                .opsForValue()
                .get(key);

//        2. 比对code，然后如果正确，登陆30天
        return Objects.equals(verificationCode, emailLoginOrRegisterDTO.getCode());

    }

    @Override
    public Boolean login(PasswordLoginDTO passwordLoginDTO) {

//        1. 数据库中获取密码，比对经过加密后的密码，
        LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserDO::getEmail, passwordLoginDTO.getPassword());

        UserDO userDO = userMapper.selectOne(wrapper);
//        2. 比对，
        if (Objects.isNull(userDO)) {
            return false;
        }else{
            if(passwordEncoder.matches(passwordLoginDTO.getPassword(),userDO.getPassword())) {


                return true;
            }else{
                return false;
            }
        }
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
