package xyz.nanian.owl.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger信息配置 (统一配置到 common 模块)
 *
 * @author slnt23
 * @since 2025/11/21
 */
@Configuration
public class SpringdocConfig {

    /**
     * 1. 配置全局信息
     * 使用 OpenAPI Bean 定义全局的文档标题、描述、版本和联系人信息。
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("NaNian Owl - 统一接口文档")
                        .description("本API文档集成了火龙果电商和用户中心等所有模块的接口。")
                        .version("开发版0.0.1")
                        .contact(new Contact()
                                .name("sln23")
                                .email("ir0211@outlook.com")
                        )
                );
    }


    /**
     * 2. 配置用户中心 API 分组
     * 启用 GroupedOpenApi 来创建不同的分组。
     * 确保您的用户中心接口路径匹配这里的 `/api/user/**` 或其他实际路径。
     */
    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("用户中心-user") // 分组名称
                .pathsToMatch("/user/**") // 匹配 user 模块的接口路径
                .packagesToScan("xyz.nanian.owl.user.controller")
                .build();
    }

    /**
     * 3. 配置火龙果电商 API 分组
     * 您可以为其他模块创建额外的 GroupedOpenApi Bean。
     */
    @Bean
    public GroupedOpenApi tradeApi() {
        return GroupedOpenApi.builder()
                .group("电商-trade")
                .pathsToMatch("/pitaya/**")
                .packagesToScan("xyz.nanian.owl.pitaya.consumer.controller")
                .build();
    }
}
