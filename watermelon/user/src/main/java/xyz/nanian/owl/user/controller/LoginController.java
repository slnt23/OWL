package xyz.nanian.owl.user.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.nanian.owl.constant.ResultStatus;
import xyz.nanian.owl.result.Result;
import xyz.nanian.owl.user.LoginApi;
import xyz.nanian.owl.user.dto.EmailLoginOrRegisterDTO;
import xyz.nanian.owl.user.dto.PasswordLoginDTO;
import xyz.nanian.owl.user.dto.SendCodeDTO;
import xyz.nanian.owl.user.service.LoginService;

/**
 * 登录相关
 * 注册相关的，
 *
 * @author slnt23
 * @since 2026/4/9
 */

@Slf4j
@RestController
@RequestMapping("/auth")
@Tag(name = "登陆管理",description = "有关登陆，注册的")
public class LoginController implements LoginApi {

    LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * 用户注册
     * @param user 用户DTO
     * @return
     */
    @Override
    @PostMapping("/register")
    @Operation(summary = "提交注册申请",description = "用户注册")
    public Result<ResultStatus> registerUser(@RequestBody @Validated EmailLoginOrRegisterDTO user) {

//        校验验证码，然后，生成用户，
        if(loginService.saveUser(user)){
            return Result.success();
        }else{
            return Result.fail();
        }
    }

    /**
     * 发送邮箱验证邮件
     * 这里从重发改为发送，实现复用，
     *
     * @param codeDTO
     * @return
     */
    @Override
    @PostMapping("/send-verification")
    @Operation(summary = "验证码发送",description = "用于发送验证邮件")
    public Result<ResultStatus> sendVerificationCode(@RequestBody @Validated SendCodeDTO codeDTO) {

        if(loginService.sendVerificationCode(codeDTO)){
            return Result.success();
        }else {
            return Result.fail();
        }
    }


    /**
     * 用户登录，邮箱验证码
     * @param user
     * @return
     */
    @Override
    @GetMapping("/login-email")
    @Operation(summary = "登陆-验证码",description = "通过验证码登陆")
    public Result<String> loginVerifyEmail(EmailLoginOrRegisterDTO user) {

        return Result.success(loginService.login(user));
    }

    /**
     * 用户登陆，验证密码，
     * @param user
     * @return
     */
    @Override
    @GetMapping("/login-password")
    @Operation(summary = "登陆-密码",description = "通过密码登陆")
    public Result<String> loginVerifyPassword(PasswordLoginDTO user) {

        return Result.success(loginService.login(user));
    }

}
