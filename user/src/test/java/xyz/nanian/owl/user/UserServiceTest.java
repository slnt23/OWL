package xyz.nanian.owl.user;


import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.nanian.owl.user.dto.UserRegisterDTO;
import xyz.nanian.owl.user.service.UserService;

/**
 * 测试用户逻辑层
 *
 * @author slnt23
 * @since 2025/11/15
 */

@Slf4j
@SpringBootTest
public class UserServiceTest {

    @Resource
    UserService userService;


    public UserRegisterDTO createUser(){
        UserRegisterDTO user=new UserRegisterDTO();
        user.setUserName("明敏");
        user.setPassword("123456");
        user.setPhone("110");
        user.setEmail("qq.com");
        user.setRole(1);
        return user;
    }

//    @Test
    public void testUserService() {

        log.info("测试注册，创建用户对象，");
        UserRegisterDTO user=createUser();
        log.info("创建用户");
        userService.saveUser(user);
    }

    @Test
    @DisplayName("测试登陆")
    public void testServiceLogin(){

        UserRegisterDTO user=createUser();
        boolean result= userService.login("110","123456");
        log.info("结果{}", result);
    }

    @Test
    public void testServiceUpdateInfo(){
        UserRegisterDTO user=createUser();
    }
}
