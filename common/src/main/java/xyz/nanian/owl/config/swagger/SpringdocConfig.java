package xyz.nanian.owl.config.swagger;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * swagger 3.*版本，使用springdoc，配备knife4j 4.5.0，
 *
 * @author slnt23
 * @since 2025/11/10
 */

@Configuration
public class SpringdocConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("火龙果电商接口文档")
                        .version("1.0.0")
                        .description("Spring API 描述"));
    }
}
