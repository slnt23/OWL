package xyz.nanian.owl.infra.rabbitmq.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static xyz.nanian.owl.infra.rabbitmq.constant.RabbitMQConstant.OPERATION_LOG_EXCHANGE;
import static xyz.nanian.owl.infra.rabbitmq.constant.RabbitMQConstant.OPERATION_LOG_QUEUE;
import static xyz.nanian.owl.infra.rabbitmq.constant.RabbitMQConstant.OPERATION_LOG_ROUTING_KEY;

/**
 * RabbitMQ declarations for operation logs.
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
        return new Jackson2JsonMessageConverter();
    }
}
