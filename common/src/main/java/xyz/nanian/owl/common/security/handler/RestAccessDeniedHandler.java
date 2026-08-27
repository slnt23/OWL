package xyz.nanian.owl.common.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import xyz.nanian.owl.common.result.Result;
import xyz.nanian.owl.common.result.ResultStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 权限不足时的 JSON 403 处理器。
 *
 * <p>当用户已通过认证但角色权限不足（例如普通用户访问后台管理接口）时，
 * Spring Security 调用本处理器，统一返回 {@link ResultStatus#FORBIDDEN}（403）
 * 格式的 JSON 响应。</p>
 *
 * <p>与 {@link RestAuthenticationEntryPoint} 的区别：</p>
 * <ul>
 *   <li>{@code RestAuthenticationEntryPoint} — 未认证（401），Token 缺失或无效</li>
 *   <li>{@code RestAccessDeniedHandler} — 已认证但权限不足（403），角色不匹配</li>
 * </ul>
 *
 * @author slnt23
 * @since 2026/8/3
 */
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    /**
     * 注入 JSON 序列化组件。
     *
     * @param objectMapper Jackson ObjectMapper
     */
    public RestAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 已认证但权限不足时统一返回 HTTP 403 与 Result&lt;T&gt;。
     *
     * @param request              当前 HTTP 请求
     * @param response             当前 HTTP 响应
     * @param accessDeniedException 权限不足异常
     * @throws IOException 写入响应失败时抛出
     */
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(Result.fail(ResultStatus.FORBIDDEN)));
    }
}