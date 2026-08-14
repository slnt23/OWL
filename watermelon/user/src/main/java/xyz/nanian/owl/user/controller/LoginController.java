package xyz.nanian.owl.user.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.nanian.owl.common.result.Result;
// [TO_BE_DELETED] import xyz.nanian.owl.user.domain.dto.EmailLoginOrRegisterDTO;
import xyz.nanian.owl.user.domain.dto.EmailLoginDTO;
import xyz.nanian.owl.user.domain.dto.PasswordLoginDTO;
import xyz.nanian.owl.user.domain.dto.ResetPasswordDTO;
import xyz.nanian.owl.user.domain.dto.SendCodeDTO;
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
@RequestMapping("/api/auth")
@Tag(name = "登陆管理",description = "有关登陆，注册")
public class LoginController {

    LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    // [TO_BE_DELETED] 旧注册接口，已由邮箱验证码登录自动注册取代。
    // @PostMapping("/register")
    // @Operation(summary = "提交注册申请", description = "用户注册")
    // @Deprecated
    // public Result<String> registerUser(@RequestBody @Validated EmailLoginOrRegisterDTO user) {
    //     return Result.success(loginService.saveUser(user));
    // }

    // [TO_BE_DELETED] 旧验证码发送路径，请使用 /auth/send-code。
    // @PostMapping("/send-verification")
    // @Operation(summary = "验证码发送", description = "用于发送验证邮件")
    // @Deprecated
    // public Result<String> sendVerificationCode(@RequestBody @Validated SendCodeDTO codeDTO) {
    //     return loginService.sendVerificationCode(codeDTO);
    // }

    // [TO_BE_DELETED] 旧邮箱验证码登录，已由下方通用 /auth/login-email 取代。
    // @PostMapping("/login-email")
    // @Operation(summary = "登陆-验证码", description = "通过验证码登陆")
    // @Deprecated
    // public Result<String> loginVerifyEmail(@RequestBody @Validated EmailLoginOrRegisterDTO user) {
    //     return Result.success(loginService.login(user));
    // }

    /**
     * [UPGRADE] 邮箱验证码登录；未注册邮箱自动创建账号，role 入参已移除。
     */
    @PostMapping("/login-email")
    @Operation(summary = "登陆注册-验证码",description = "邮箱验证码登录自动注册")
    public Result<String> loginVerifyEmail(@RequestBody @Validated EmailLoginDTO user) {
        return Result.success(loginService.login(user));
    }

    /**
     * [UPGRADE] 密码登录，role 入参已废弃，从数据库读取。
     */
    @PostMapping("/login-password")
    @Operation(summary = "登陆-密码",description = "通过密码登陆")
    public Result<String> loginVerifyPassword(@RequestBody @Validated PasswordLoginDTO user) {
        return Result.success(loginService.login(user));
    }

    /**
     * [UPGRADE] 新验证码发送接口。
     */
    @PostMapping("/send-code")
    @Operation(summary = "验证码发送",description = "验证码发送接口")
    public Result<String> sendCode(@RequestBody @Validated SendCodeDTO codeDTO) {
        return loginService.sendVerificationCode(codeDTO);
    }

    /**
     * [UPGRADE] 忘记密码重置。
     */
    @PostMapping("/password/reset")
    @Operation(summary = "重置密码",description = "通过邮箱验证码重置密码")
    public Result<String> resetPassword(@RequestBody @Validated ResetPasswordDTO resetPasswordDTO) {
        loginService.resetPassword(resetPasswordDTO);
        return Result.success();
    }

    /**
     * [UPGRADE] 登出，使当前 token 失效。
     */
    @PostMapping("/logout")
    @Operation(summary = "登出",description = "当前 token 加入黑名单")
    public Result<String> logout(@RequestHeader(value = "Authorization", required = false) String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            loginService.logout(authorization.substring(7));
        }
        return Result.success();
    }

}
