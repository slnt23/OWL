package xyz.nanian.owl.common.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Boot 4 默认提供 Jackson 3 的 ObjectMapper，旧业务代码仍使用 Jackson 2 API，
 * 这里补充一个 Jackson 2 ObjectMapper Bean 保证兼容。
 *
 * @author slnt23
 * @since 2026/8/14
 */
@Configuration
public class JacksonConfig {

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
