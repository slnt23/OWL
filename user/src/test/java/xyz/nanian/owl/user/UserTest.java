package xyz.nanian.owl.user;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 用户测试
 *
 * @author slnt23
 * @since 2025/11/19
 */

@Slf4j
public class UserTest {

    public final static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public static void main(String[] args) {

        String password="123456";

        log.info("加密密码");
        String newPassword= passwordEncoder.encode(password);

        log.info("加密后");
        System.out.println(newPassword);

        log.info("对比");
        System.out.println(passwordEncoder.matches(password,newPassword));
    }
}
