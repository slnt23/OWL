package xyz.nanian.owl.interceptor;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 全局拦截器,
 * 这里在配置中注册拦截器
 *
 * @author slnt23
 * @since 2025/12/10
 */

@Component
public class AuthInterceptor implements HandlerInterceptor {

    /**
     *
     * @param request 请求对象
     * @param response 相应对象
     * @param handler 处理器
     * @return true 成功，false 拦截
     * @throws Exception 异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {

    }
}
