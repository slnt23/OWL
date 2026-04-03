package xyz.nanian.owl.interceptor;


import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import xyz.nanian.owl.exception.BizException;
import xyz.nanian.owl.utils.jwt.JwtUtil;
import xyz.nanian.owl.utils.jwt.UserContext;

import java.util.concurrent.TimeUnit;

import static xyz.nanian.owl.constant.RedisConstant.LOGIN_KEY;
import static xyz.nanian.owl.constant.RedisConstant.LOGIN_TIME_OUT;

/**
 * 登陆认证
 *
 * @author slnt23
 * @since 2026/1/20
 */

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private static RedisTemplate redisTemplate;

    public LoginInterceptor(RedisTemplate redisTemplate) {
        LoginInterceptor.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        if(!(handler instanceof HandlerMethod)){
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new BizException(HttpStatus.UNAUTHORIZED,"登录异常");
        }

        token = token.substring(7);

        Claims claims = JwtUtil.parseToken(token);
        Long userId = claims.get("userId", Long.class);
        String userPhone = claims.get("userPhone", String.class);

        UserContext.setUserId(userId);

        String key = LOGIN_KEY + userPhone;
        Boolean exists = redisTemplate.hasKey(key);
        if(Boolean.TRUE.equals(exists)){
            redisTemplate.expire(key,LOGIN_TIME_OUT, TimeUnit.MINUTES);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {

        UserContext.clear();
    }
}
