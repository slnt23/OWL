package xyz.nanian.owl.config.rabbitmq;


import org.springframework.amqp.core.*;
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
public class RabbitMQConfig {

    @Bean
    public Queue fanoutQueue() {
        return new Queue(FANOUT_QUEUE);
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

//    订单监听
    @Bean
    public Queue orderQueue() {
        return new Queue(ORDER_QUEUE,true);
    }

    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Binding orderBinding() {
        return BindingBuilder
                .bind(orderQueue())
                .to(orderExchange())
                .with(ORDER_ROUTING_KEY);
    }

//    业务日志广播，TODO 目前先测试，实验好了在创建架构，
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
                .to(fanoutExchange());
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }


}
