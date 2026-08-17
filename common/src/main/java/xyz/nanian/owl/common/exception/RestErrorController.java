package xyz.nanian.owl.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.nanian.owl.common.result.Result;
import xyz.nanian.owl.common.result.ResultStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 兜底错误控制器。
 * 所有未被业务代码处理的错误统一转发到 /error，这里保证始终返回 JSON，
 * 不暴露默认 Whitelabel 页面、堆栈或内部路径信息。
 *
 * @author slnt23
 * @since 2026/8/15
 */
@Slf4j
@Controller
public class RestErrorController implements ErrorController {

    private static final String ERROR_STATUS_CODE_ATTRIBUTE = "jakarta.servlet.error.status_code";

    private final ObjectMapper objectMapper;

    public RestErrorController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 统一错误响应。
     *
     * @param request  当前请求
     * @param response 响应对象
     * @throws IOException 写响应失败
     */
    @RequestMapping("/error")
    public void handleError(HttpServletRequest request,
                            HttpServletResponse response) throws IOException {
        Object statusCodeAttribute = request.getAttribute(ERROR_STATUS_CODE_ATTRIBUTE);
        int statusCode = statusCodeAttribute instanceof Integer code ? code : HttpStatus.INTERNAL_SERVER_ERROR.value();
        HttpStatus status = HttpStatus.resolve(statusCode);
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        log.warn("统一错误响应：path={}, status={}", request.getRequestURI(), status.value());

        if (!response.isCommitted()) {
            response.setStatus(status.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write(objectMapper.writeValueAsString(Result.fail(resolveResultStatus(status))));
        }
    }

    private ResultStatus resolveResultStatus(HttpStatus status) {
        return switch (status.value()) {
            case 400 -> ResultStatus.BAD_REQUEST;
            case 401 -> ResultStatus.UNAUTHORIZED;
            case 403 -> ResultStatus.FORBIDDEN;
            case 404 -> ResultStatus.NOT_FOUND;
            case 405 -> ResultStatus.METHOD_NOT_ALLOWED;
            case 503 -> ResultStatus.SERVICE_UNAVAILABLE;
            default -> ResultStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
