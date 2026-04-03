package xyz.nanian.owl.pitaya;


import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * mq
 *
 * @author slnt23
 * @since 2026/1/28
 */

@SpringBootTest
//@RequiredArgsConstructor
public class RabbitMQTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void sentMessage(){
        String msg = "Hello World!";
        String exchange = "biz_log_exchange";
        rabbitTemplate.convertAndSend(exchange, msg);

    }

}
