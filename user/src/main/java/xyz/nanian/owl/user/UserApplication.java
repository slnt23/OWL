package xyz.nanian.owl.user;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 用户启动类
 *
 * @author slnt23
 * @since 2025/11/19
 */

@SpringBootApplication
@ComponentScan(basePackages = {"xyz.nanian.owl"})
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
