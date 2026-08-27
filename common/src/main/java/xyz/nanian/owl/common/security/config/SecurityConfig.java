package xyz.nanian.owl.common.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpMethod;
import xyz.nanian.owl.common.security.filter.JwtAuthenticationFilter;
import xyz.nanian.owl.common.security.JwtTokenProvider;
import xyz.nanian.owl.common.security.RoleConstants;
import xyz.nanian.owl.common.security.TokenRevocationService;
import xyz.nanian.owl.common.security.handler.RestAccessDeniedHandler;
import xyz.nanian.owl.common.security.handler.RestAuthenticationEntryPoint;

/**
 * Spring Security 安全配置。
 *
 * <p>本配置类定义整个应用的安全策略，包括：</p>
 * <ul>
 *   <li>关闭 CSRF（API 服务无需 CSRF 防护）</li>
 *   <li>启用 CORS 默认配置</li>
 *   <li>使用无状态会话（STATELESS），不创建 HttpSession</li>
 *   <li>配置接口白名单（登录、公开接口、文档、静态资源）</li>
 *   <li>博客公开读接口 GET 免登录，写接口需认证</li>
 *   <li>后台管理接口（/api/admin/**）要求 ADMIN 角色</li>
 *   <li>其余接口默认要求登录</li>
 *   <li>注册 {@link JwtAuthenticationFilter} 在 UsernamePasswordAuthenticationFilter 之前执行</li>
 *   <li>认证失败 → 返回 JSON 401，权限不足 → 返回 JSON 403</li>
 * </ul>
 *
 * <p>后续如需拆分为独立 security 模块，可整体迁移本包。</p>
 *
 * @author slnt23
 * @since 2026/8/3
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final RestAccessDeniedHandler restAccessDeniedHandler;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectProvider<TokenRevocationService> tokenRevocationServiceProvider;

    /**
     * 注入安全配置所需的组件。
     *
     * @param restAuthenticationEntryPoint 未认证 / Token 无效时的 JSON 401 处理器
     * @param restAccessDeniedHandler      已认证但权限不足时的 JSON 403 处理器
     * @param jwtTokenProvider             JWT 生成与解析组件
     * @param tokenRevocationServiceProvider 可选的 Token 撤销服务提供者
     */
    public SecurityConfig(RestAuthenticationEntryPoint restAuthenticationEntryPoint,
                          RestAccessDeniedHandler restAccessDeniedHandler,
                          JwtTokenProvider jwtTokenProvider,
                          ObjectProvider<TokenRevocationService> tokenRevocationServiceProvider) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.restAccessDeniedHandler = restAccessDeniedHandler;
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenRevocationServiceProvider = tokenRevocationServiceProvider;
    }

    /**
     * 配置 Spring Security 过滤链。
     *
     * <p>关闭 CSRF、启用 CORS、使用无状态会话；
     * 配置登录、文档和公共接口白名单，后台管理接口要求 ADMIN 角色，
     * 其余接口默认要求登录，并在认证失败时统一返回 JSON。</p>
     *
     * @param http HttpSecurity 配置对象
     * @return 配置完成的 SecurityFilterChain
     * @throws Exception 构建过滤链失败时抛出
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                // [UPGRADE] public auth endpoints only; logout stays authenticated
                                "/api/auth/send-code",
                                "/api/auth/login-email",
                                "/api/auth/login-password",
                                "/api/auth/password/reset",
                                "/api/public",
                                "/api/public/**",
                                "/doc.html",
                                "/doc.html/**",
                                "/webjars/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/v3/api-docs/**",
                                "/error",
                                "/favicon.ico",
                                "/api/admin/feature/**",
                                "/api/admin/spotlight/**"
                        ).permitAll()
                        // [mango] 博客公开读接口：GET 免登录，写接口保持默认要求登录
                        .requestMatchers(HttpMethod.GET, "/api/blog/**").permitAll()
                        .requestMatchers("/api/admin/**").hasRole(RoleConstants.ADMIN)
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(restAuthenticationEntryPoint)
                        .accessDeniedHandler(restAccessDeniedHandler))
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, tokenRevocationServiceProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * 密码编码器，统一使用 BCrypt 对密码进行加密和校验。
     *
     * @return BCryptPasswordEncoder 实例
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}