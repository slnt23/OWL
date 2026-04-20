package xyz.nanian.owl.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.ai.model.ApiKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.web.method.HandlerMethod;

import java.util.Collections;
import java.util.List;

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

    final String securitySchemeName = "jwtAuth";   // 方案名称，可自定义

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
//                目前来说下面的配置没有生效，问题未知,不，是生效了,但是是全局的，
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Authorization")
                                        .description("请输入 Bearer Token,格式：(Bearer开头)Bearer xxx")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
    }

//    /**
//     * 全局为每个接口添加请求头部输入框（关键配置）
//     * 这会让所有接口的“请求头部”标签下出现可输入框
//     */
//    @Bean
//    public OperationCustomizer customGlobalHeaders() {
//        return (Operation operation, HandlerMethod handlerMethod) -> {
//
//            // 添加 Authorization 请求头部输入框
//            Parameter authorizationHeader = new Parameter()
//                    .in("header")                                      // 指定为 Header
//                    .name("Authorization")                             // Header 名称
//                    .description("请输入 Bearer Token（格式：Bearer xxxxx）")
//                    .required(false)                                   // 是否必填，可改为 true
//                    .schema(new StringSchema());                       // 输入类型为字符串
//
//            operation.addParametersItem(authorizationHeader);
//
//            // 如果还需要添加其他 Header，可以继续添加，例如：
//            // Parameter traceIdHeader = new Parameter()
//            //         .in("header")
//            //         .name("X-Trace-Id")
//            //         .description("链路追踪ID")
//            //         .required(false)
//            //         .schema(new StringSchema());
//            // operation.addParametersItem(traceIdHeader);
//
//            return operation;
//        };
//    }

//    /**
//     * 构建权限协议列表
//     * @return 认证协议列表
//     */
//    @Bean
//    private static List<SecurityScheme> securitySchemes() {
//        return Collections.singletonList(
//                new ApiKey("Authorization", "Authorization", "header"));
//    }

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
