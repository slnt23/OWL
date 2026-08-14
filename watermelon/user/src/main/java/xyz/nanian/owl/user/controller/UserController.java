package xyz.nanian.owl.user.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.nanian.owl.common.result.ResultStatus;
import xyz.nanian.owl.common.result.Result;
import xyz.nanian.owl.user.domain.dto.EmailBindDTO;
import xyz.nanian.owl.user.domain.dto.PasswordUpdateDTO;
// [TO_BE_DELETED] import xyz.nanian.owl.user.domain.dto.UserInfoDTO;
import xyz.nanian.owl.user.domain.dto.UserInfoUpdateDTO;
import xyz.nanian.owl.user.domain.vo.UserInfoVO;
import xyz.nanian.owl.user.service.UserService;
import xyz.nanian.owl.common.security.CurrentUserContext;

/**
 * 用户相关的控制器方法,
 * 使用@Rest Controller注解，是@Responsed注解和@Controller注解的结合，会自动的将返回值序列化为Json格式，
 *
 * @author slnt23
 * @since 2025/11/13
 */

@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "用户管理", description = "有关用户个人的一系列controller")
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @GetMapping("/info")
    @Operation(summary = "获取用户信息")
    public Result<UserInfoVO> getUserInfo() {
        return Result.success(userService.getUserInfoByCode());
    }


    // [TO_BE_DELETED] 旧资料更新接口，请使用 PUT /user/info。
    // @PutMapping("/userInfo")
    // @Operation(summary = "用户信息更新")
    // @Deprecated
    // public Result<ResultStatus> updateUser(@RequestBody UserInfoDTO userInfoDTO) {
    //     if (userService.updateUserInfo(userInfoDTO)) {
    //         return Result.success();
    //     } else {
    //         return Result.fail();
    //     }
    // }

    // [TO_BE_DELETED] 旧改密接口，请使用 PUT /user/password。
    // @PutMapping("/password/{password}")
    // @Operation(summary = "用户密码更新")
    // @Deprecated
    // public Result<ResultStatus> updatePassword(@PathVariable String password) {
    //     if (userService.updateUserPassword(password)) {
    //         return Result.success();
    //     } else {
    //         return Result.fail();
    //     }
    // }

    // [TO_BE_DELETED] 空实现，用户搜索后续交给 administration 模块。
    // @GetMapping("/users")
    // @Operation(summary = "用户搜索")
    // @Deprecated
    // public Result<ResultStatus> searchUser(String name) {
    //     return Result.fail(ResultStatus.API_UN_IMPL);
    // }

    /**
     * 更新用户头像
     *
     * @param file
     * @return
     */
    @PutMapping("/avatar")
    @Operation(summary = "用户头像更新")
    public Result<String> updateAvatar(@RequestParam("file") MultipartFile file) {
        String userCode = CurrentUserContext.getUserCode();
//        检查用户是否登录
        if (userCode == null) {
            return Result.fail(ResultStatus.UNAUTHORIZED);
        }
        String avatarUrl = userService.updateUserAvatar(file, userCode);

        return Result.success(avatarUrl);
    }

    /**
     * [UPGRADE] 更新用户资料，仅允许 userName/nickname/phone/remark。
     */
    @PutMapping("/info")
    @Operation(summary = "用户资料更新-升级")
    public Result<ResultStatus> updateUserInfo(@RequestBody @Validated UserInfoUpdateDTO userInfoUpdateDTO) {
        if (userService.updateUserInfo(userInfoUpdateDTO)) {
            return Result.success();
        }
        return Result.fail();
    }

    /**
     * [UPGRADE] 换绑邮箱。
     */
    @PutMapping("/email")
    @Operation(summary = "换绑邮箱-升级")
    public Result<ResultStatus> updateEmail(@RequestBody @Validated EmailBindDTO emailBindDTO) {
        if (userService.updateUserEmail(emailBindDTO)) {
            return Result.success();
        }
        return Result.fail();
    }

    /**
     * [UPGRADE] 登录后修改密码。
     */
    @PutMapping("/password")
    @Operation(summary = "修改密码-升级")
    public Result<ResultStatus> updatePassword(@RequestBody @Validated PasswordUpdateDTO passwordUpdateDTO) {
        if (userService.updateUserPassword(passwordUpdateDTO)) {
            return Result.success();
        }
        return Result.fail();
    }

}
