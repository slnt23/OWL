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
import org.springframework.web.filter.OncePerRequestFilter;
import xyz.nanian.owl.common.result.ResultStatus;
import xyz.nanian.owl.common.security.CurrentUserContext;
import xyz.nanian.owl.common.security.JwtConstants;
import xyz.nanian.owl.common.security.JwtTokenProvider;
import xyz.nanian.owl.common.security.LoginUser;
import xyz.nanian.owl.common.security.RoleConstants;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * JWT 认证过滤器
 * 解析 Authorization: Bearer <token>，校验通过后写入 SecurityContext
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String JWT_ERROR_STATUS_ATTRIBUTE = "JWT_ERROR_STATUS";

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            try {
                Claims claims = jwtTokenProvider.parseToken(header.substring(7));

                LoginUser loginUser = new LoginUser();
                loginUser.setUserId(claims.get(JwtConstants.CLAIM_USER_ID, Long.class));
                loginUser.setUserCode(claims.get(JwtConstants.CLAIM_USER_CODE, String.class));
                loginUser.setEmail(claims.get(JwtConstants.CLAIM_USER_EMAIL, String.class));
                loginUser.setRoleName(claims.get(JwtConstants.CLAIM_ROLE, String.class));

                List<GrantedAuthority> authorities = buildAuthorities(loginUser.getRoleName());
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(loginUser, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                CurrentUserContext.setLoginUser(loginUser);
            } catch (ExpiredJwtException e) {
                request.setAttribute(JWT_ERROR_STATUS_ATTRIBUTE, ResultStatus.TOKEN_EXPIRED);
            } catch (JwtException | IllegalArgumentException e) {
                request.setAttribute(JWT_ERROR_STATUS_ATTRIBUTE, ResultStatus.TOKEN_INVALID);
            }
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            CurrentUserContext.clear();
            SecurityContextHolder.clearContext();
        }
    }

    private List<GrantedAuthority> buildAuthorities(String roleName) {
        if (roleName == null || roleName.isBlank()) {
            return List.of();
        }
        String authority = RoleConstants.ROLE_PREFIX + roleName.trim().toUpperCase(Locale.ROOT);
        return List.of(new SimpleGrantedAuthority(authority));
    }
}
