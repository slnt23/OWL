package xyz.nanian.owl.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xyz.nanian.owl.interceptor.AuthInterceptor;

/**
 * 用于注册全局拦截器,相关视频可以看以前SSM的那个 TODO 后续需要根据具体的，自己的请求路径进行更改，
 *
 * @author slnt23
 * @since 2025/12/10
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**") //拦截路径
                .excludePathPatterns("/login"); //排除的拦截路径
    }
}
