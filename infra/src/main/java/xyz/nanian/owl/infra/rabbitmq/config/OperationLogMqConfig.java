package xyz.nanian.owl.infra.rabbitmq.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static xyz.nanian.owl.infra.rabbitmq.constant.RabbitMQConstants.OPERATION_LOG_EXCHANGE;
import static xyz.nanian.owl.infra.rabbitmq.constant.RabbitMQConstants.OPERATION_LOG_QUEUE;
import static xyz.nanian.owl.infra.rabbitmq.constant.RabbitMQConstants.OPERATION_LOG_ROUTING_KEY;

/**
 * RabbitMQ 操作日志消息声明配置。
 *
 * <p>声明持久化队列、交换机与绑定关系，并使用 Spring AMQP 4 的
 * {@link JacksonJsonMessageConverter} 进行 JSON 消息转换。</p>
 *
 * @author slnt23
 * @since 2026/1/27
 */
@Configuration
public class OperationLogMqConfig {

    @Bean
    public Queue operationLogQueue() {
        return new Queue(OPERATION_LOG_QUEUE, true);
    }

    @Bean
    public DirectExchange operationLogExchange() {
        return new DirectExchange(OPERATION_LOG_EXCHANGE);
    }

    @Bean
    public Binding operationLogBinding() {
        return BindingBuilder
                .bind(operationLogQueue())
                .to(operationLogExchange())
                .with(OPERATION_LOG_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
