package xyz.nanian.owl.interceptor;


import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import xyz.nanian.owl.exception.BizException;
import xyz.nanian.owl.utils.jwt.JwtUtil;
import xyz.nanian.owl.utils.jwt.UserContext;

import java.util.Collections;
import java.util.stream.Collectors;

/**
 * 登陆认证
 *
 * @author slnt23
 * @since 2026/1/20
 */

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        if(!(handler instanceof HandlerMethod)){
            return true;
        }

        log.info("LoginInteger 拦截请求：{}",request.getRequestURI());
        log.info("Request: method={}, uri={}, headers={}, params={}",
                request.getMethod(),
                request.getRequestURI(),
                Collections.list(request.getHeaderNames()).stream()
                        .collect(Collectors.toMap(
                                h -> h,
                                h -> request.getHeader(h)
                        )),
                request.getParameterMap()
        );

        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new BizException(HttpStatus.UNAUTHORIZED,"登录异常");
        }

        token = token.substring(7);

        Claims claims = JwtUtil.parseToken(token);
        Long userId = claims.get("userId", Long.class);

        UserContext.setUserId(userId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {

        UserContext.clear();
    }
}
