package xyz.nanian.owl.common.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 定义Spring MVC的各种功能
 * 安全拦截已迁移至 Spring Security（common.security.config.SecurityConfig）
 *
 * @author slnt23
 * @since 2025/12/10
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 解决跨域问题，
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")                    // 所有接口
                .allowedOriginPatterns("*")           // 允许所有域名（生产环境建议改为具体域名）
                .allowedMethods("*")                  // 允许所有请求方法
                .allowedHeaders("*")                  // 关键：允许所有请求头（包括 Authorization）
                .allowCredentials(true)               // 允许携带 Cookie / Authorization
                .maxAge(3600);                        // 预检请求缓存时间（秒）
    }

}
