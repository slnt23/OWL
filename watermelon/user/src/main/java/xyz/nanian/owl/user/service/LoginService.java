package xyz.nanian.owl.user.service;


import xyz.nanian.owl.common.result.Result;
// [TO_BE_DELETED] import xyz.nanian.owl.user.domain.dto.EmailLoginOrRegisterDTO;
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
     * 发送邮箱验证码，同一邮箱 5 分钟内只能发送一次。
     *
     * @param sendCodeDTO 邮箱信息
     * @return 统一返回结果
     */
    Result<String> sendVerificationCode(SendCodeDTO sendCodeDTO);

    // [TO_BE_DELETED] 注册新用户；已升级为邮箱登录自动注册。
    // @Deprecated
    // String saveUser(EmailLoginOrRegisterDTO emailLoginOrRegisterDTO);

    // [TO_BE_DELETED] 旧邮箱验证码登录；已升级为 login(EmailLoginDTO)。
    // @Deprecated
    // String login(EmailLoginOrRegisterDTO emailLoginOrRegisterDTO);

    /**
     * 密码登录，角色从数据库读取。
     *
     * @param passwordLoginDTO 邮箱和密码
     * @return JWT token
     */
    String login(PasswordLoginDTO passwordLoginDTO);

    /**
     * 邮箱验证码登录；未注册邮箱自动创建默认 USER 角色账号。
     *
     * @param emailLoginDTO 邮箱和验证码
     * @return JWT token
     */
    String login(EmailLoginDTO emailLoginDTO);

    /**
     * 通过邮箱验证码重置密码，成功后使旧 token 失效。
     *
     * @param resetPasswordDTO 邮箱、验证码和新密码
     */
    void resetPassword(ResetPasswordDTO resetPasswordDTO);

    /**
     * 登出，将当前 token 的 jti 加入 Redis 黑名单。
     *
     * @param token Bearer token
     */
    void logout(String token);
}
