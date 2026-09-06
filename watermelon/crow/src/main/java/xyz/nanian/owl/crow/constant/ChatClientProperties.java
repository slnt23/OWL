package xyz.nanian.owl.crow.constant;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * chat属性
 *
 * @author slnt23
 * @since 2026/5/12
 */

@Data
@Component
@ConfigurationProperties(prefix = "spring.ai.chat.client")
public class ChatClientProperties {
    private String defaultSystem;
    private Context context = new Context();

    @Data
    public static class Context {
        private int maxHistoryMessages = 20;
        private int maxContextTokens = 4000;
    }
}