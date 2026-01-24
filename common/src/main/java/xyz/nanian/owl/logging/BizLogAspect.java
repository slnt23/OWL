package xyz.nanian.owl.logging;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import xyz.nanian.owl.utils.jwt.UserContext;

/**
 * 业务日志
 *
 * @author slnt23
 * @since 2026/1/15
 */

@Component
@Slf4j
@Aspect
public class BizLogAspect {

    @Around("@annotation(bizLog)")
    public Object around(ProceedingJoinPoint joinPoint,BizLog bizLog) throws Throwable {

//        执行前
        long startTime = System.currentTimeMillis();
        Long userId = UserContext.getUserId();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String method = signature.getDeclaringType()
                .getSimpleName() + "." + signature.getName();

        try{
//            调用目标方法
            Object result = joinPoint.proceed();

//            执行后
            log.info(
                    "业务成功 module={}，action={}，userId={}，method={}，cost={}ms",
                    bizLog.module(),
                    bizLog.action(),
                    userId,
                    method,
                    System.currentTimeMillis() - startTime
            );

            return result;
        } catch(Exception e ){
            log.error(
                    "业务失败 module={}，action={}，userId={}，method={}，cost={}ms",
                    bizLog.module(),
                    bizLog.action(),
                    userId,
                    method,
                    System.currentTimeMillis() - startTime,
                    e
            );
            throw e;
        }
    }
}
