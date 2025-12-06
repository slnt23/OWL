package xyz.nanian.owl.user.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * springboot security 密码加密，相关配置
 *
 * @author slnt23
 * @since 2025/11/14
 */

@Configuration
public class SecurityConfig {

    /**
     * 密码加密配置，
     * @return BCrypt密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
