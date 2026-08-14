package xyz.nanian.owl.user.service;


import xyz.nanian.owl.common.result.Result;
import xyz.nanian.owl.user.domain.dto.EmailLoginOrRegisterDTO;
import xyz.nanian.owl.user.domain.dto.EmailLoginDTO;
import xyz.nanian.owl.user.domain.dto.PasswordLoginDTO;
import xyz.nanian.owl.user.domain.dto.ResetPasswordDTO;
import xyz.nanian.owl.user.domain.dto.SendCodeDTO;

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
     * [TO_BE_DELETED] 注册新用户；已升级为邮箱登录自动注册，请使用 /auth/login-email。
     */
    @Deprecated
    String saveUser(EmailLoginOrRegisterDTO emailLoginOrRegisterDTO);

    /**
     * [TO_BE_DELETED] 旧邮箱验证码登录；已升级为 login(EmailLoginDTO)。
     */
    @Deprecated
    String login(EmailLoginOrRegisterDTO emailLoginOrRegisterDTO);

    /**
     * [UPGRADE] 密码登录，role 入参已废弃，从数据库读取。
     */
    String login(PasswordLoginDTO passwordLoginDTO);

    /**
     * [UPGRADE] 邮箱验证码登录，未注册邮箱自动创建默认 USER 账号。
     */
    String login(EmailLoginDTO emailLoginDTO);

    /**
     * [UPGRADE] 忘记密码重置。
     */
    void resetPassword(ResetPasswordDTO resetPasswordDTO);

    /**
     * [UPGRADE] 登出，使当前 token 失效。
     */
    void logout(String token);

}
