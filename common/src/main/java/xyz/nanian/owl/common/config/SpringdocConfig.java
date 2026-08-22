package xyz.nanian.owl.common.config;

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
 * OpenAPI / Knife4j 文档配置。
 * 统一放在 common 模块，按业务模块生成独立 API 分组。
 *
 * @author slnt23
 * @since 2025/11/21
 */
@Configuration
public class SpringdocConfig {

    final String securitySchemeName = "JwtAuth";

    /**
     * 全局 OpenAPI 信息：标题、描述、版本、JWT 安全方案。
     */
    @Bean
    public OpenAPI openAllAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("OWL（猫头鹰）统一后端接口文档")
                        .description("集成用户认证/个人中心、AI 对话、价格追踪、电商交易与后台管理模块的接口文档。")
                        .version("v0.0.1（开发版）")
                        .contact(new Contact()
                                .name("OWL 开发团队")
                        )
                )
                // 全局 JWT 安全方案，所有 API 分组共用
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Authorization")
                                        .description("登录接口返回 token 后，在请求头 Authorization 中携带：Bearer <token>")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
    }

    /**
     * 用户中心 API 分组：认证、验证码、个人资料、收货地址。
     */
    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("用户中心-user")
                .packagesToScan("xyz.nanian.owl.user.controller")
                .build();
    }

    /**
     * 电商中心 API 分组：消费者端与商家端商品、购物车、订单。
     */
    @Bean
    public GroupedOpenApi pitayaApi() {
        return GroupedOpenApi.builder()
                .group("电商中心-pitaya")
                .packagesToScan(
                        "xyz.nanian.owl.pitaya.consumer.controller",
                        "xyz.nanian.owl.pitaya.merchant.controller")
                .build();
    }

    /**
     * 后台管理中心 API 分组：用户、角色、内容配置。
     */
    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("后台管理中心-admin")
                .packagesToScan("xyz.nanian.owl.admin.controller")
                .build();
    }

    /**
     * 价格中心 API 分组：价格追踪、比价、来源与地区对比。
     */
    @Bean
    public GroupedOpenApi sugarcaneApi() {
        return GroupedOpenApi.builder()
                .group("价格中心-sugarcane")
                .packagesToScan("xyz.nanian.owl.sugarcane.controller")
                .build();
    }
    /**
     * AI 中心 API 分组：对话、会话管理。
     */
    @Bean
    public GroupedOpenApi crowApi() {
        return GroupedOpenApi.builder()
                .group("AI中心-crow")
                .packagesToScan("xyz.nanian.owl.crow.controller")
                .build();
    }

    /**
     * 博客中心 API 分组：文章、分类、标签、个人信息、教育、技能。
     */
    @Bean
    public GroupedOpenApi mangoApi() {
        return GroupedOpenApi.builder()
                .group("博客中心-mango")
                .packagesToScan("xyz.nanian.owl.mango.controller")
                .build();
    }

}
