package xyz.nanian.owl.common.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.web.filter.OncePerRequestFilter;
import xyz.nanian.owl.common.result.ResultStatus;
import xyz.nanian.owl.common.security.CurrentUserContext;
import xyz.nanian.owl.common.security.JwtConstants;
import xyz.nanian.owl.common.security.JwtTokenProvider;
import xyz.nanian.owl.common.security.LoginUser;
import xyz.nanian.owl.common.security.RoleConstants;
import xyz.nanian.owl.common.security.TokenRevocationService;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * JWT 认证过滤器。
 *
 * <p>在每次 HTTP 请求进入时，从 {@code Authorization: Bearer <token>} 头中提取 JWT，
 * 依次校验签名有效性、过期时间、撤销状态（jti 黑名单）与 token 版本号（tokenVersion），
 * 校验通过后将 {@link LoginUser} 写入 {@link CurrentUserContext} 和 {@link SecurityContextHolder}，
 * 请求结束后统一清理 ThreadLocal 与 SecurityContext。</p>
 *
 * <h3>认证流程</h3>
 * <ol>
 *   <li>从请求头提取 Bearer Token</li>
 *   <li>调用 {@link JwtTokenProvider#parseToken(String)} 解析 Claims</li>
 *   <li>构建 {@link LoginUser} 对象</li>
 *   <li>校验 token 是否可用（未被撤销、版本号匹配）</li>
 *   <li>写入 SecurityContext 与 CurrentUserContext</li>
 * </ol>
 *
 * <h3>异常处理</h3>
 * <ul>
 *   <li>{@link ExpiredJwtException} → 设置 {@link ResultStatus#TOKEN_EXPIRED}</li>
 *   <li>{@link JwtException} / 格式错误 → 设置 {@link ResultStatus#TOKEN_INVALID}</li>
 *   <li>撤销/版本不匹配 → 设置 {@link ResultStatus#TOKEN_INVALID}</li>
 * </ul>
 *
 * <p>所有异常不会直接抛出，而是将错误状态写入 request 属性，
 * 由 {@link xyz.nanian.owl.common.security.handler.RestAuthenticationEntryPoint} 统一返回 JSON 401。</p>
 *
 * @author slnt23
 * @since 2026/8/3
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /** 请求属性名，用于向认证入口点传递 TOKEN_EXPIRED / TOKEN_INVALID 状态 */
    public static final String JWT_ERROR_STATUS_ATTRIBUTE = "JWT_ERROR_STATUS";

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRevocationService tokenRevocationService;

    /**
     * 使用 JwtTokenProvider 创建过滤器，不启用撤销与版本校验。
     *
     * @param jwtTokenProvider JWT 生成与解析组件
     */
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this(jwtTokenProvider, null);
    }

    /**
     * 使用 JwtTokenProvider 与可选的撤销服务创建过滤器。
     *
     * @param jwtTokenProvider              JWT 生成与解析组件
     * @param revocationServiceProvider     提供 TokenRevocationService 的 ObjectProvider，可为空
     */
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
                                   ObjectProvider<TokenRevocationService> revocationServiceProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenRevocationService = revocationServiceProvider == null
                ? null
                : revocationServiceProvider.getIfAvailable();
    }

    /**
     * 执行请求认证逻辑。
     *
     * <p>从请求头读取 Bearer Token 并解析 Claims，校验通过后写入安全上下文；
     * 请求处理完成后清理 ThreadLocal 与 SecurityContext。</p>
     *
     * @param request     当前 HTTP 请求
     * @param response    当前 HTTP 响应
     * @param filterChain 过滤器链
     * @throws ServletException 过滤链执行失败时抛出
     * @throws IOException      过滤链执行失败时抛出
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // ==================== 1. 获取请求信息 ====================
        String header = request.getHeader("Authorization");
        String uri = request.getRequestURI();

        // -------------------- 日志：请求进入过滤器 --------------------
        log.debug("JWT 过滤器: {} {}, Authorization={}",
                request.getMethod(), uri,
                header == null ? "无" : header.substring(0, Math.min(header.length(), 60)) + "...");

        // ==================== 2. 解析并校验 Token ====================
        if (header != null && header.startsWith("Bearer ")) {
            try {
                Claims claims = jwtTokenProvider.parseToken(header.substring(7));

                // 构建登录用户对象
                LoginUser loginUser = new LoginUser();
                loginUser.setUserId(claims.get(JwtConstants.CLAIM_USER_ID, Long.class));
                loginUser.setUserCode(claims.get(JwtConstants.CLAIM_USER_CODE, String.class));
                loginUser.setEmail(claims.get(JwtConstants.CLAIM_USER_EMAIL, String.class));
                loginUser.setRoleName(claims.get(JwtConstants.CLAIM_ROLE, String.class));

                if (!isTokenUsable(claims, loginUser)) {
                    // -------------------- 日志：token 被撤销或版本不匹配 --------------------
                    log.warn("JWT token 不可用(撤销/版本不匹配): userId={}, uri={}",
                            loginUser.getUserId(), uri);

                    request.setAttribute(JWT_ERROR_STATUS_ATTRIBUTE, ResultStatus.TOKEN_INVALID);
                } else {
                    // -------------------- 日志：认证成功 --------------------
                    log.debug("JWT 认证成功: userId={}, role={}, uri={}",
                            loginUser.getUserId(), loginUser.getRoleName(), uri);

                    buildAuthentication(request, loginUser);
                }
            } catch (ExpiredJwtException e) {
                // -------------------- 日志：token 已过期 --------------------
                log.warn("JWT token 已过期: uri={}, 过期时间={}",
                        uri, e.getClaims().getExpiration());

                request.setAttribute(JWT_ERROR_STATUS_ATTRIBUTE, ResultStatus.TOKEN_EXPIRED);
            } catch (JwtException | IllegalArgumentException e) {
                // -------------------- 日志：token 无效(签名错误/格式错误) --------------------
                log.warn("JWT token 无效: uri={}, 原因={}",
                        uri, e.getMessage());

                request.setAttribute(JWT_ERROR_STATUS_ATTRIBUTE, ResultStatus.TOKEN_INVALID);
            }
        } else if (header != null) {
            // -------------------- 日志：Authorization 头格式错误 --------------------
            log.warn("Authorization 头格式不正确: uri={}, header={}",
                    uri, header);
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            CurrentUserContext.clear();
            SecurityContextHolder.clearContext();
        }
    }

    private boolean isTokenUsable(Claims claims, LoginUser loginUser) {
        String jti = claims.get(JwtConstants.CLAIM_JTI, String.class);
        Long tokenVersion = claims.get(JwtConstants.CLAIM_TOKEN_VERSION, Long.class);
        if (tokenRevocationService == null || jti == null || tokenVersion == null
                || loginUser.getUserId() == null) {
            return true;
        }
        return !tokenRevocationService.isRevoked(jti)
                && tokenRevocationService.getTokenVersion(loginUser.getUserId()) == tokenVersion;
    }

    private void buildAuthentication(HttpServletRequest request, LoginUser loginUser) {
        List<GrantedAuthority> authorities = buildAuthorities(loginUser.getRoleName());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(loginUser, null, authorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        CurrentUserContext.setLoginUser(loginUser);
    }

    private List<GrantedAuthority> buildAuthorities(String roleName) {
        if (roleName == null || roleName.isBlank()) {
            return List.of();
        }
        String authority = RoleConstants.ROLE_PREFIX + roleName.trim().toUpperCase(Locale.ROOT);
        return List.of(new SimpleGrantedAuthority(authority));
    }
}