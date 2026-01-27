package service.Impl;


import DO.BizLogDO;
import DTO.BizLogMessage;
import lombok.RequiredArgsConstructor;
import mapper.BizLogMapper;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import service.handleBizLogService;

import static xyz.nanian.owl.constant.RabbitMQConstant.*;

/**
 * 业务日志记录处理
 *
 * @author slnt23
 * @since 2026/1/27
 */

@Service
@RequiredArgsConstructor
public class handleBizLogServiceImpl implements handleBizLogService {

    private final BizLogMapper bizLogMapper;

    /**
     * 监听订单消息，
     * @param message
     */
    @Override
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = BIZ_LOG_QUEUE),
            exchange = @Exchange(name = BIZ_LOG_EXCHANGE,type = ExchangeTypes.FANOUT)
    ))
    public void addBizLog(BizLogMessage message) {

        BizLogDO bizLogDO = new BizLogDO();
        BeanUtils.copyProperties(message,bizLogDO);

        bizLogMapper.insert(bizLogDO);


    }
}
