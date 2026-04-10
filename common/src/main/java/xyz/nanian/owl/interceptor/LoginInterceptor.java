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
import xyz.nanian.owl.constant.ExceptionConstant;
import xyz.nanian.owl.exception.BizException;
import xyz.nanian.owl.utils.jwt.JwtUtil;
import xyz.nanian.owl.utils.jwt.UserContext;
import xyz.nanian.owl.utils.jwt.UserInfo;

import java.util.concurrent.TimeUnit;

import static xyz.nanian.owl.constant.RedisConstant.LOGIN_KEY;
import static xyz.nanian.owl.constant.RedisConstant.LOGIN_TIME_OUT;

/**
 * 登陆认证，拦截器
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
            throw new BizException(HttpStatus.UNAUTHORIZED, ExceptionConstant.OPERATION_FAIL);
        }

        token = token.substring(7);

        Claims claims = JwtUtil.parseToken(token);
        String userCode = claims.get("userCode", String.class);
        String userEmail = claims.get("userEmail", String.class);

        UserInfo userInfo = new UserInfo();
        userInfo.setUserCode(userCode);

        UserContext.setUserInfo(userInfo);

        String key = LOGIN_KEY + userEmail;
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
