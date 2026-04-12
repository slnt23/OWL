package xyz.nanian.owl.config.interceptor;


import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xyz.nanian.owl.interceptor.AuthInterceptor;
import xyz.nanian.owl.interceptor.LoginInterceptor;

/**
 * 用于注册全局拦截器,相关视频可以看以前SSM的那个 TODO 后续需要根据具体的，自己的请求路径进行更改，
 * 1. 这里拦截器可以给后续的token验证身份
 * 2. 后续添加新拦截器到拦截器的包里面
 *
 * @author slnt23
 * @since 2025/12/10
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private AuthInterceptor authInterceptor;

    @Resource
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/user",
                        "/user/login",
                        "/user/register",
//                        "/pitaya/**",
                        "/public",
                        "/doc.html",
                        "/doc.html/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/error",
                        "/auth/**"
                );

//        这里对于全局拦截器没有使用，暂时去除，
//        registry.addInterceptor(authInterceptor)
//                .addPathPatterns("/**") //拦截路径
//                .excludePathPatterns("/login"); //排除的拦截路径

    }

}
