package xyz.nanian.owl.config.rabbitmq;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static xyz.nanian.owl.constant.RabbitMQConstant.*;

/**
 * rabbitMQ配置
 *
 * @author slnt23
 * @since 2026/1/27
 */

@Configuration
public class BizMQConfig {

//    业务日志广播，
    @Bean
    public Queue bizQueue() {
        return new Queue(BIZ_LOG_QUEUE,true);
    }

    @Bean
    public DirectExchange bizExchange() {
        return new DirectExchange(BIZ_LOG_EXCHANGE);
    }

    @Bean
    public Binding bizBinding() {
        return BindingBuilder
                .bind(bizQueue())
                .to(bizExchange())
                .with(BIZ_LOG_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }


}
