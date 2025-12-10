package xyz.nanian.owl.interceptor;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 全局拦截器 TODO 全局拦截器的设置，用于登陆验证，请求日志，链路追踪,后续设置登陆相关补充
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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 在 Controller 方法执行之前执行
//        String token = request.getHeader("Authorization");
//        if (token == null || !token.startsWith("Bearer ")) {
//            response.setStatus(401);
//            response.setContentType("application/json;charset=UTF-8");
//            response.getWriter().write(
//                    JSONObject.toJSONString(Result.create("未登录或登录已过期", ResultStatus.UNAUTHORIZED))
//            );
//            return false; // 拦截请求，不继续向下执行
//        }
        // 验证通过，继续执行 Controller
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
