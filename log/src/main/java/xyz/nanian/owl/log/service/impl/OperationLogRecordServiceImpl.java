package xyz.nanian.owl.log.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.log.domain.entity.AdminLogDO;
import xyz.nanian.owl.log.domain.entity.BizLogDO;
import xyz.nanian.owl.log.domain.entity.UserLogDO;
import xyz.nanian.owl.log.domain.message.OperationLogMessage;
import xyz.nanian.owl.log.mapper.AdminLogMapper;
import xyz.nanian.owl.log.mapper.BizLogMapper;
import xyz.nanian.owl.log.mapper.UserLogMapper;
import xyz.nanian.owl.log.service.OperationLogRecordService;

import java.time.LocalDateTime;

import static xyz.nanian.owl.infra.rabbitmq.constant.RabbitMQConstants.OPERATION_LOG_EXCHANGE;
import static xyz.nanian.owl.infra.rabbitmq.constant.RabbitMQConstants.OPERATION_LOG_QUEUE;
import static xyz.nanian.owl.infra.rabbitmq.constant.RabbitMQConstants.OPERATION_LOG_ROUTING_KEY;

@Service
@RequiredArgsConstructor
public class OperationLogRecordServiceImpl implements OperationLogRecordService {

    private final BizLogMapper bizLogMapper;
    private final UserLogMapper userLogMapper;
    private final AdminLogMapper adminLogMapper;

    @Override
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = OPERATION_LOG_QUEUE),
            exchange = @Exchange(name = OPERATION_LOG_EXCHANGE),
            key = OPERATION_LOG_ROUTING_KEY
    ))
    public void addOperationLog(OperationLogMessage message) {
        if (message == null || message.getLogType() == null) {
            return;
        }
        switch (message.getLogType()) {
            case BIZ -> saveBizLog(message);
            case USER -> saveUserLog(message);
            case ADMIN -> saveAdminLog(message);
        }
    }

    private void saveBizLog(OperationLogMessage message) {
        BizLogDO bizLog = new BizLogDO();
        bizLog.setModule(message.getModule());
        bizLog.setAction(message.getAction());
        bizLog.setUserId(message.getOperatorId());
        bizLog.setMethod(message.getMethod());
        bizLog.setSuccess(message.getSuccess());
        bizLog.setCost(message.getCost());
        bizLog.setErrorMsg(message.getErrorMsg());
        bizLog.setTraceId(message.getTraceId());
        bizLog.setCreateTime(LocalDateTime.now());
        bizLogMapper.insert(bizLog);
    }

    private void saveUserLog(OperationLogMessage message) {
        UserLogDO userLog = new UserLogDO();
        userLog.setUserId(message.getOperatorId());
        userLog.setModule(message.getModule());
        userLog.setAction(message.getAction());
        userLog.setMethod(message.getMethod());
        userLog.setSuccess(message.getSuccess());
        userLog.setCost(message.getCost());
        userLog.setErrorMsg(message.getErrorMsg());
        userLog.setTraceId(message.getTraceId());
        userLog.setCreateTime(LocalDateTime.now());
        userLogMapper.insert(userLog);
    }

    private void saveAdminLog(OperationLogMessage message) {
        AdminLogDO adminLog = new AdminLogDO();
        adminLog.setAdminId(message.getOperatorId());
        adminLog.setAction(message.getAction());
        adminLog.setDetail(message.getModule() + " - " + message.getAction());
        adminLog.setCreateTime(LocalDateTime.now());
        adminLogMapper.insert(adminLog);
    }
}
