//package xyz.nanian.owl.user;
//
//
//import jakarta.annotation.Resource;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import xyz.nanian.owl.user.domain.dto.SendCodeDTO;
//import xyz.nanian.owl.user.domain.dto.UserInfoDTO;
//import xyz.nanian.owl.user.service.UserService;
//
///**
// * 测试用户逻辑层
// *
// * @author slnt23
// * @since 2025/11/15
// */
//
//@Slf4j
//@SpringBootTest
//public class UserServiceTest {
//
//    @Resource
//    UserService userService;
//
//
//    public SendCodeDTO createUser(){
//        SendCodeDTO user=new SendCodeDTO();
//        user.setUserName("明敏");
//        user.setPassword("123456");
//        user.setPhone("110");
//        user.setEmail("qq.com");
//        user.setRole(1);
//        return user;
//    }
//
//    public UserInfoDTO createUserInfo(){
//        UserInfoDTO user=new UserInfoDTO();
//        user.setUserName("李白");
//        user.setNickName("小白");
//        user.setEmail("qq.com");
//        user.setPhone("12345");
//        user.setRemark("大诗人");
//        user.setRawPhone("120");
//        return user;
//    }
//
//    @Test
//    public void testUserService() {
//
//        log.info("测试注册，创建用户对象，");
//        SendCodeDTO user=createUser();
//        log.info("创建用户");
//        userService.saveUser(user);
//    }
//
//    @Test
//    @DisplayName("测试登陆")
//    public void testServiceLogin(){
//
////        UserRegisterDTO user=createUser();
////        boolean result= userService.login("110","123456");
////        log.info("结果{}", result);
//    }
//
//    @Test
//    @DisplayName("测试更新用户信息，")
//    public void testServiceUpdateInfo(){
//
//        UserInfoDTO user=createUserInfo();
//        log.info("测试更新用户信息{}",userService.updateUserInfo(user));
//    }
//}
