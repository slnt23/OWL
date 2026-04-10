package xyz.nanian.owl.user.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import xyz.nanian.owl.constant.ResultStatus;
import xyz.nanian.owl.result.Result;
import xyz.nanian.owl.user.UserApi;
import xyz.nanian.owl.user.dto.UserInfoDTO;
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
public class UserController implements UserApi {

//    private final static Logger log= LoggerFactory.getLogger(UserController.class);
//    添加注解@Slf4j后，相当于自动生成这一行，

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

//    /**
//     * 注册用户
//     * @param user 用户DTO
//     */
//    @Override
//    @PostMapping("/register")
//    @Operation(summary = "用户注册",description = "详细描述：注册")
//    public Result<ResultStatus> registerUser(@RequestBody @Validated UserRegisterDTO user) {
//
//        if(!RegexUtil.isPhone(user.getPhone())){
//            return Result.fail(ResultStatus.PARAMS_INVALID);
//        }
//
//        if(userService.saveUser(user)){
//            return Result.success(ResultStatus.SUCCESS);
//        }else {
//            return Result.fail(ResultStatus.FAIL);
//        }
//    }
//
//    /**
//     * 用户登陆
//     * @param phone 用户手机号
//     * @param password 用户密码
//     */
//    @Override
//    @GetMapping("/login")
//    @Operation(summary = "用户登陆")
//    public Result<String> loginUser(@RequestParam String phone, @RequestParam String password) {
//
//        if(!RegexUtil.isPhone(phone)){
//            return Result.fail();
//        }
//
//        String result = userService.login(phone,password);
//        if(result == null){
//            return Result.fail();
//        }else {
//            return Result.success(result);
//        }
//    }

    /**
     * 搜索用户通过用户名
     * @param name 用户名
     * @return 包含用户数据的分页格式，
     */
    @Override
    @GetMapping("/users")
    @Operation(summary = "用户搜索")
    public Result<ResultStatus> searchUserByName(String name) {
        return null;
    }

    /**
     * 更新用户信息
     * @param userInfoDTO 用户最新信息
     * @return message
     */
    @Override
    @PutMapping("/userInfo")
    @Operation(summary = "用户信息更新")
    public Result<ResultStatus> updateUser(@RequestBody UserInfoDTO userInfoDTO) {

        if(userService.updateUserInfo(userInfoDTO)){
            return Result.success();
        }else{
            return Result.fail();
        }
    }

    @Override
    @PutMapping("/avatar")
    @Operation(summary = "用户头像更新")
    public Result<ResultStatus> updateAvatarByCode(String Phone) {
        return null;
    }

    /**
     * 更新用户密码通过手机号，或者邮箱，TODO 这里暂时没添加邮箱，手机号是自己填写的，所有不安全，
     * @param Phone phone
     * @param newPassword newPassword
     * @return message
     */
    @Override
    @PutMapping("/password")
    @Operation(summary = "用户密码更新")
    public Result<ResultStatus> updatePasswordByCode(String Phone,String newPassword) {

        if(userService.updateUserPassword(Phone,newPassword)){
            return Result.success();
        }else{
            return Result.fail();
        }
    }
}
