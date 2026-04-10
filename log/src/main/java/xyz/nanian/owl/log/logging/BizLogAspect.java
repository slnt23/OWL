package xyz.nanian.owl.log.logging;


import xyz.nanian.owl.log.DTO.BizLogMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import xyz.nanian.owl.utils.jwt.UserContext;

import static xyz.nanian.owl.constant.RabbitMQConstant.*;

/**
 * 业务日志
 *
 * @author slnt23
 * @since 2026/1/15
 */

@Component
@Slf4j
@Aspect
@RequiredArgsConstructor
public class BizLogAspect {

    private final RabbitTemplate rabbitTemplate;

    @Around("@annotation(bizLog)")
    public Object around(ProceedingJoinPoint joinPoint,BizLog bizLog) throws Throwable {

//        执行前
        long startTime = System.currentTimeMillis();
        String userCode = UserContext.getUserCode();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String method = signature
                .getDeclaringType()
                .getSimpleName() + "." + signature.getName();

        try{
            // 调用目标方法
            Object result = joinPoint.proceed();

            // 构造成功日志消息
            BizLogMessageDTO msg = new BizLogMessageDTO();
            msg.setModule(bizLog.module());
            msg.setAction(bizLog.action());
            msg.setUserCode(userCode);
            msg.setMethod(method);
            msg.setSuccess(true);
            msg.setCost(System.currentTimeMillis() - startTime);
            msg.setTraceId(MDC.get("TraceId"));
            msg.setErrorMsg(null);

            System.out.println(msg);

            // 发送 MQ（异步日志）
            rabbitTemplate.convertAndSend(
                    BIZ_LOG_EXCHANGE,
                    BIZ_LOG_ROUTING_KEY,
                    msg
            );

            return result;
        } catch(Exception e ){
            // 构造失败日志消息
            BizLogMessageDTO msg = new BizLogMessageDTO();
            msg.setModule(bizLog.module());
            msg.setAction(bizLog.action());
            msg.setUserCode(userCode);
            msg.setMethod(method);
            msg.setSuccess(false);
            msg.setCost(System.currentTimeMillis() - startTime);
            msg.setTraceId(MDC.get("TraceId"));
            msg.setErrorMsg(e.getMessage());

            System.out.println(msg);

            // 发送失败日志到 MQ
            rabbitTemplate.convertAndSend(
                    BIZ_LOG_EXCHANGE,
                    BIZ_LOG_ROUTING_KEY,
                    msg
            );
            throw e;
        }
    }
}
