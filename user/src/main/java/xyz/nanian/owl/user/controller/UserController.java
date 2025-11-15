package xyz.nanian.owl.user.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.nanian.owl.result.Result;
import xyz.nanian.owl.user.UserApi;
import xyz.nanian.owl.user.dto.UserInfoDTO;
import xyz.nanian.owl.user.dto.UserRegisterDTO;
import xyz.nanian.owl.user.service.UserService;

/**
 * 用户相关的控制器方法,
 * 使用@Rest Controller注解，是@Responsed注解和@Controller注解的结合，会自动的将返回值序列化为Json格式，
 *
 * @author slnt23
 * @since 2025/11/13
 */


@RestController("/user")
public class UserController implements UserApi {

    private final static Logger log= LoggerFactory.getLogger(UserController.class);

    private final UserService userService;


    /**
     * 构造器注入
     * @param userService 用户逻辑编辑
     */
    UserController(UserService userService) {

        this.userService = userService;

    }


    /**
     * 注册用户
     * @param user 用户DTO
     */
    @Override
    @GetMapping("/new-user")
    public Result<String> registerUser(UserRegisterDTO user) {
        log.info("新用户注册中");

        userService.saveUser(user);

        return Result.success();
    }

    /**
     * 用户登陆验证
     * @param username 用户名
     * @param password 用户密码
     */
    @Override
    @GetMapping("/single-user")
    public Result<String> loginUser(String username, String password) {

        if(userService.login(username, password)){
            return  Result.success();
        }else{
            return Result.fail();
        }
    }

    @Override
    public Result<Object> searchUserByName(String name) {
        return null;
    }

    /**
     * 更新用户信息
     * @param userInfoDTO 用户最新信息
     * @param rawPhone 原手机号，或者正在使用的
     * @return message
     */
    @Override
    @PostMapping("/single-user")
    public Result<Object> updateUserByName(UserInfoDTO userInfoDTO,String rawPhone) {
        return null;
    }

    @Override
    @PostMapping("/avatar")
    public Result<String> updateAvatarByCode(String Phone) {
        return null;
    }

    @Override
    @PostMapping("/password")
    public Result<String> updatePasswordByCode(String Phone) {
        return null;
    }
}
