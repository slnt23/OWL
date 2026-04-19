package xyz.nanian.owl.user.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import xyz.nanian.owl.constant.ResultStatus;
import xyz.nanian.owl.result.Result;
import xyz.nanian.owl.user.domain.dto.UserInfoDTO;
import xyz.nanian.owl.user.service.UserService;

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
@Tag(name = "用户管理",description = "有关用户个人的一系列controller")
public class UserController {

//    private final static Logger log= LoggerFactory.getLogger(UserController.class);
//    添加注解@Slf4j后，相当于自动生成这一行，

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 更新用户信息
     * @param userInfoDTO 用户最新信息
     * @return message
     */
    @PutMapping("/userInfo")
    @Operation(summary = "用户信息更新")
    public Result<ResultStatus> updateUser(@RequestBody UserInfoDTO userInfoDTO) {

        if(userService.updateUserInfo(userInfoDTO)){
            return Result.success();
        }else{
            return Result.fail();
        }
    }

    /**
     * 更新用户密码通过手机号，或者邮箱，
     * 这里应该通过登录状态获取UserCode，然后直接更改密码，-- 后续改为邮箱验证码，
     * TODO 这里暂时没添加邮箱，手机号是自己填写的，所有不安全，-- 后续改为邮箱验证码，
     *
     * @param Phone phone
     * @param newPassword newPassword
     * @return message
     */
    @PutMapping("/password")
    @Operation(summary = "用户密码更新")
    public Result<ResultStatus> updatePasswordByCode(String Phone,String newPassword) {

        if(userService.updateUserPassword(Phone,newPassword)){
            return Result.success();
        }else{
            return Result.fail();
        }
    }

    /**
     * 搜索用户通过用户名/或者用户UserCode
     *
     * @param name 用户名
     * @return 包含用户数据的分页格式，
     */
    @GetMapping("/users")
    @Operation(summary = "用户搜索")
    public Result<ResultStatus> searchUserByName(String name) {
        return null;
    }

    /**
     *更新用户头像
     * @param Phone
     * @return
     */
    @PutMapping("/avatar")
    @Operation(summary = "用户头像更新")
    public Result<ResultStatus> updateAvatarByCode(String Phone) {
        return null;
    }


}
