package xyz.nanian.owl.log.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import xyz.nanian.owl.common.security.CurrentUserContext;
import xyz.nanian.owl.log.annotation.OperationLog;
import xyz.nanian.owl.log.domain.message.OperationLogMessage;
import xyz.nanian.owl.log.filter.TraceIdFilter;

import static xyz.nanian.owl.infra.rabbitmq.constant.RabbitMQConstants.OPERATION_LOG_EXCHANGE;
import static xyz.nanian.owl.infra.rabbitmq.constant.RabbitMQConstants.OPERATION_LOG_ROUTING_KEY;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class OperationLogAspect {

    private final RabbitTemplate rabbitTemplate;

    @Around("@annotation(operationLog)")
    public Object around(ProceedingJoinPoint joinPoint, OperationLog operationLog) throws Throwable {
        long startTime = System.currentTimeMillis();
        Long operatorId = CurrentUserContext.getUserId();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String method = signature.getDeclaringType().getSimpleName()
                + "#" + signature.getName();

        boolean success = false;
        String errorMsg = null;
        try {
            Object result = joinPoint.proceed();
            success = true;
            return result;
        } catch (Throwable e) {
            errorMsg = e.getMessage();
            throw e;
        } finally {
            long cost = System.currentTimeMillis() - startTime;
            log.info("[human] operation type={} module={} action={} method={} success={} cost={}ms persist={} traceId={}",
                    operationLog.type(), operationLog.module(), operationLog.action(),
                    method, success, cost, operationLog.persist(), MDC.get(TraceIdFilter.TRACE_ID));
            if (operationLog.persist()) {
                sendMessage(operationLog, operatorId, method, success, cost, errorMsg);
            }
        }
    }

    private void sendMessage(OperationLog operationLog, Long operatorId,
                             String method, boolean success, long cost, String errorMsg) {
        OperationLogMessage message = new OperationLogMessage();
        message.setLogType(operationLog.type());
        message.setModule(operationLog.module());
        message.setAction(operationLog.action());
        message.setOperatorId(operatorId);
        message.setMethod(method);
        message.setSuccess(success);
        message.setCost(cost);
        message.setErrorMsg(errorMsg);
        message.setTraceId(MDC.get(TraceIdFilter.TRACE_ID));

        try {
            rabbitTemplate.convertAndSend(
                    OPERATION_LOG_EXCHANGE,
                    OPERATION_LOG_ROUTING_KEY,
                    message
            );
        } catch (Exception e) {
            log.error("[human] send operation log failed, module={} action={}", operationLog.module(), operationLog.action(), e);
        }
    }
}
