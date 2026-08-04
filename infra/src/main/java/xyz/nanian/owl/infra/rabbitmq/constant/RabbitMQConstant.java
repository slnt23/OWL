package xyz.nanian.owl.infra.rabbitmq.constant;


/**
 * rabbitmq常量
 *
 * @author slnt23
 * @since 2026/1/27
 */

public class RabbitMQConstant {

    public static final String FANOUT_EXCHANGE = "fanout_exchange";
    public static final String FANOUT_QUEUE = "fanout_queue";


    public static final String ORDER_QUEUE = "order_queue";
    public static final String ORDER_EXCHANGE = "order_exchange";
    public static final String ORDER_ROUTING_KEY = "order_routing_key";


    public static final String OPERATION_LOG_QUEUE = "operation_log_queue";
    public static final String OPERATION_LOG_EXCHANGE = "operation_log_exchange";
    public static final String OPERATION_LOG_ROUTING_KEY = "operation_log_routing_key";

}
