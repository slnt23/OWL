package xyz.nanian.owl.interceptor;


import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import xyz.nanian.owl.exception.LoginException;
import xyz.nanian.owl.result.ResultStatus;
import xyz.nanian.owl.exception.BizException;
import xyz.nanian.owl.utils.jwt.JwtUtil;
import xyz.nanian.owl.utils.jwt.UserContext;
import xyz.nanian.owl.utils.jwt.UserInfo;

/**
 * 登陆认证，拦截器
 *
 * @author slnt23
 * @since 2026/1/20
 */

@Slf4j
@Component
//@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

//    private static RedisTemplate<String,Object> redisTemplate;
//    public LoginInterceptor(RedisTemplate redisTemplate) {
//        LoginInterceptor.redisTemplate = redisTemplate;
//    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        if(!(handler instanceof HandlerMethod)){
            return true;
        }

//        log.info("request为：{}", request.getHeader());
//          TODO 这里token为null,
        String token = request.getHeader("Authorization");

//        log.info("token代码为:{}", token);

        if (token == null || !token.startsWith("Bearer ")) {
            throw new LoginException(ResultStatus.TOKEN_EXPIRED);
        }

        token = token.substring(7);

        try {
//            解析token
            Claims claims = JwtUtil.parseToken(token);

            Long userId = claims.get("userId", Long.class);
            String userCode = claims.get("userCode", String.class);
            String userEmail = claims.get("userEmail", String.class);
//            用户登录上下文注入信息，
            UserInfo userInfo = new UserInfo();
            userInfo.setUserCode(userCode);
            userInfo.setUserId(userId);
            userInfo.setEmail(userEmail);

            UserContext.setUserInfo(userInfo);

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new LoginException(ResultStatus.TOKEN_EXPIRED);
        } catch (io.jsonwebtoken.JwtException e) {
            throw new LoginException(ResultStatus.TOKEN_INVALID);
        }

//        这里是伪无状态了，因为就算是redis中没有key，依旧可以解析成功，后续改为双token时再用这个，
//        并且还要在登陆代码中写将key，注入redis,
//        String key = LOGIN_KEY + userEmail;
//        Boolean exists = redisTemplate.hasKey(key);
//        if(exists){
//            redisTemplate.expire(key,LOGIN_TIME_OUT, TimeUnit.MINUTES);
//        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler, Exception ex) {

        UserContext.clear();
    }
}
