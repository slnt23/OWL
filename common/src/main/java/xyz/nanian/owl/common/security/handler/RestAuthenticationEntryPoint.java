package xyz.nanian.owl.common.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import xyz.nanian.owl.common.result.Result;
import xyz.nanian.owl.common.result.ResultStatus;
import xyz.nanian.owl.common.security.filter.JwtAuthenticationFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 未认证 / Token 无效时的 JSON 401 处理器。
 *
 * <p>当请求未携带有效 Token 或 Token 校验失败时，Spring Security 将调用本处理器，
 * 统一返回 {@link ResultStatus#UNAUTHORIZED}（401）格式的 JSON 响应。</p>
 *
 * <p>优先读取 {@link JwtAuthenticationFilter} 写入的 request 属性
 * {@code JWT_ERROR_STATUS}，以区分"Token 过期"和"Token 无效"两种场景：</p>
 * <ul>
 *   <li>{@link ResultStatus#TOKEN_EXPIRED} — Token 已过期</li>
 *   <li>{@link ResultStatus#TOKEN_INVALID} — Token 非法、被撤销或版本不匹配</li>
 *   <li>未设置 → 默认 {@link ResultStatus#UNAUTHORIZED}</li>
 * </ul>
 *
 * @author slnt23
 * @since 2026/8/3
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    /**
     * 注入 JSON 序列化组件。
     *
     * @param objectMapper Jackson ObjectMapper
     */
    public RestAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 未认证或 Token 无效时统一返回 HTTP 401 与 Result&lt;T&gt;。
     *
     * <p>优先读取 JwtAuthenticationFilter 写入的错误状态，
     * 例如 TOKEN_EXPIRED、TOKEN_INVALID，未设置时默认返回 UNAUTHORIZED。</p>
     *
     * @param request        当前 HTTP 请求
     * @param response       当前 HTTP 响应
     * @param authException  认证异常
     * @throws IOException 写入响应失败时抛出
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        Object error = request.getAttribute(JwtAuthenticationFilter.JWT_ERROR_STATUS_ATTRIBUTE);
        ResultStatus status = error instanceof ResultStatus resultStatus ? resultStatus : ResultStatus.UNAUTHORIZED;

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(Result.fail(status)));
    }
}