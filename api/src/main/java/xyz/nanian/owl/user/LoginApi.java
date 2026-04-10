package xyz.nanian.owl.user;


import xyz.nanian.owl.constant.ResultStatus;
import xyz.nanian.owl.result.Result;
import xyz.nanian.owl.user.dto.EmailLoginOrRegisterDTO;
import xyz.nanian.owl.user.dto.PasswordLoginDTO;
import xyz.nanian.owl.user.dto.SendCodeDTO;

/**
 * 登录，注册
 *
 * @author slnt23
 * @since 2026/4/9
 */

public interface LoginApi {

    /**
     * 用户注册
     * @param user 用户DTO
     */
    Result<ResultStatus> registerUser(EmailLoginOrRegisterDTO user);

//    /**
//     * 验证邮箱链接,这里暂时取消这个，
//     * @param email
//     * @return
//     */
//    Result<ResultStatus> verifyEmail(String email);

    /**
     * 发送验证邮件
     * @param user
     * @return
     */
    Result<String> sendVerificationCode(SendCodeDTO user);


    /**
     * 用户登陆，邮箱验证码
     * TODO 了解网关，JWT等登陆 相关的模块再添加新内容或者重构 ,JWT,Session等可以做登陆状态维持
     * @param user
     */
    Result<String> loginVerifyEmail(EmailLoginOrRegisterDTO user);


    /**
     * 用户登陆，验证密码
     * @param user
     * @return
     */
    Result<String> loginVerifyPassword(PasswordLoginDTO user);
}
