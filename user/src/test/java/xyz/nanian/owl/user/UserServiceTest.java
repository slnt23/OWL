package xyz.nanian.owl.user;


import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.nanian.owl.user.dto.UserRegisterDTO;
import xyz.nanian.owl.user.entity.UserDO;
import xyz.nanian.owl.user.service.UserService;

/**
 * 测试用户逻辑层
 *
 * @author slnt23
 * @since 2025/11/15
 */

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

    @Test
    public void testUserService() {

//        测试注册
        UserRegisterDTO user=createUser();
        userService.saveUser(user);




    }
}
