package logging;


import DTO.BizLogMessage;
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

import static xyz.nanian.owl.constant.RabbitMQConstant.BIZ_LOG_EXCHANGE;
import static xyz.nanian.owl.constant.RabbitMQConstant.BIZ_LOG_QUEUE;

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
        Long userId = UserContext.getUserId();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String method = signature.getDeclaringType()
                .getSimpleName() + "." + signature.getName();

        try{
            // 调用目标方法
            Object result = joinPoint.proceed();

            // 构造成功日志消息
            BizLogMessage msg = new BizLogMessage();
            msg.setModule(bizLog.module());
            msg.setAction(bizLog.action());
            msg.setUserId(userId);
            msg.setMethod(method);
            msg.setSuccess(true);
            msg.setCost(System.currentTimeMillis() - startTime);
            msg.setTraceId(MDC.get("TraceId"));
            msg.setErrorMsg(null);

            // 发送 MQ（异步日志）
            rabbitTemplate.convertAndSend(
                    BIZ_LOG_EXCHANGE,
                    BIZ_LOG_QUEUE,
                    msg
            );
//            执行后
//            log.info(
//                    "业务成功 module={}，action={}，userId={}，method={}，cost={}ms",
//                    bizLog.module(),
//                    bizLog.action(),
//                    userId,
//                    method,
//                    System.currentTimeMillis() - startTime
//            );

            return result;
        } catch(Exception e ){
            // 构造失败日志消息
            BizLogMessage msg = new BizLogMessage();
            msg.setModule(bizLog.module());
            msg.setAction(bizLog.action());
            msg.setUserId(userId);
            msg.setMethod(method);
            msg.setSuccess(false);
            msg.setCost(System.currentTimeMillis() - startTime);
            msg.setTraceId(MDC.get("TraceId"));
            msg.setErrorMsg(e.getMessage());

            // 发送失败日志到 MQ
            rabbitTemplate.convertAndSend(
                    BIZ_LOG_EXCHANGE,
                    BIZ_LOG_QUEUE,
                    msg
            );

            // 原异常继续抛出，不吞业务异常
//             log.error(
//                                "业务失败 module={}，action={}，userId={}，method={}，cost={}ms",
//                                bizLog.module(),
//                                bizLog.action(),
//                                userId,
//                                method,
//                                System.currentTimeMillis() - startTime,
//                                e
//                        );
            throw e;
        }
    }
}
