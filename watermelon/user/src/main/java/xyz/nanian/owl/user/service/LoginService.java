package xyz.nanian.owl.user.service;


import xyz.nanian.owl.result.Result;
import xyz.nanian.owl.user.dto.EmailLoginOrRegisterDTO;
import xyz.nanian.owl.user.dto.PasswordLoginDTO;
import xyz.nanian.owl.user.dto.SendCodeDTO;

/**
 * 登陆注册相关接口
 *
 * @author slnt23
 * @since 2026/4/9
 */

public interface LoginService {

    /**
     * 发送验证码
     * @param sendCodeDTO
     * @return
     */
    Result<String> sendVerificationCode(SendCodeDTO sendCodeDTO);

    /**
     * 注册新用户
     * @param emailLoginOrRegisterDTO 用户DTO基本信息
     */
    Boolean saveUser(EmailLoginOrRegisterDTO emailLoginOrRegisterDTO);

    /**
     * 登陆验证,邮箱验证码
     * @param emailLoginOrRegisterDTO 手机号
     * @return 密码是否正确的 bool
     */
    String login(EmailLoginOrRegisterDTO emailLoginOrRegisterDTO);

    /**
     * 登陆验证 ，密码
     * @param passwordLoginDTO
     * @return
     */
    String login(PasswordLoginDTO passwordLoginDTO);

}
