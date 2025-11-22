package xyz.nanian.owl.user.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger信息配置
 *
 * @author slnt23
 * @since 2025/11/21
 */

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "用户中心管理后台",
                description = "用户中心接口文档",
                version = "开发版0.0.1",
                contact = @Contact(
                        name = "sln23",
                        email = "ir0211@outlook.com"
                )
        )
)
public class OpenApiConfig {

//    TODO 这个配置添加之后就扫描不到接口了，可能又是版本问题，
//    @Bean
//    public GroupedOpenApi userApi() {
//        return GroupedOpenApi.builder()
//                .group("user")
//                .pathsToMatch("/api/**")
//                .build();
//    }
}
