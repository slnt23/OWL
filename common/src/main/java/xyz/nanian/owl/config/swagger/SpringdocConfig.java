package xyz.nanian.owl.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger信息配置 (统一配置到 common 模块),
 * 尽量使用指定包下，
 * 也可以使用指定路径下
 *
 * @author slnt23
 * @since 2025/11/21
 */
@Configuration
public class SpringdocConfig {

    /**
     * 配置全局信息
     * 使用 OpenAPI Bean 定义全局的文档标题、描述、版本和联系人信息。
     */
    @Bean
    public OpenAPI openAllAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("NaNian Owl - 统一接口文档")
                        .description("本API文档集成了火龙果电商和用户中心等所有模块的接口。")
                        .version("开发版0.0.1")
                        .contact(new Contact()
                                .name("sln23")
                                .email("ir0211@outlook.com")
                        )
                )
//                目前来说下面的配置没有生效，问题未知
                .components(new Components()
                        .addSecuritySchemes("jwtAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Authorization")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("jwtAuth"));
    }


    /**
     * 配置用户中心 API 分组
     * 启用 GroupedOpenApi 来创建不同的分组。
     * 确保您的用户中心接口路径匹配这里的 `/user/**` 或其他实际路径。
     */
    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("用户中心-user") // 分组名称
//                .pathsToMatch("/user/**")// 匹配 user 模块的接口路径
                .packagesToScan("xyz.nanian.owl.user.controller")//搜索特定的分路径
                .build();
    }

    /**
     * 配置火龙果电商 API 分组
     * 您可以为其他模块创建额外的 GroupedOpenApi Bean
     */
    @Bean
    public GroupedOpenApi pitayaApi() {
        return GroupedOpenApi.builder()
                .group("电商中心-pitaya")
//                .pathsToMatch("/pitaya/**")
                .packagesToScan("xyz.nanian.owl.pitaya.consumer.controller")
                .build();
    }

    /**
     * 管理员 api
     */
    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("管理员中心-admin")
                .pathsToMatch("/admin/**")
                .build();
    }

    /**
     * 有关sugarcane 价多多模块
     *
     */
    @Bean
    public GroupedOpenApi sugarcaneApi() {
        return GroupedOpenApi.builder()
                .group("价格管理中心-sugarcane")
//                .pathsToMatch("/pitaya/**")
                .packagesToScan("xyz.nanian.owl.sugarcane.controller")
                .build();
    }
    /**
     * 有关crow ai模块
     *
     */
    @Bean
    public GroupedOpenApi crowApi() {
        return GroupedOpenApi.builder()
                .group("AI助手中心-crow")
//                .pathsToMatch("/pitaya/**")
                .packagesToScan("xyz.nanian.owl.crow.controller")
                .build();
    }

}
