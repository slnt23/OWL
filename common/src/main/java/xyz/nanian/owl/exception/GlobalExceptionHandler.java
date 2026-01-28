package xyz.nanian.owl.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import xyz.nanian.owl.result.Result;
import xyz.nanian.owl.constant.ResultStatus;

import java.util.stream.Collectors;

/**
 * 全局异常处理器，
 * 日志打印的错误是提醒自己，不是返回前端的
 *
 * @author slnt23
 * @since 2025/11/19
 */

@Slf4j
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {


    /**
     * 业务异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = BizException.class)
    public Result<?> handleBizException(BizException e) {
        return Result.fail(ResultStatus.BIZ_ERROR);
    }

    /**
     * 参数校验异常
     * @param e @Valid @Validated校验
     * @return 自定义
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public Result<?> handleValidExceptionHandler(Exception e) {
        String msg = e instanceof MethodArgumentNotValidException
                ? ((MethodArgumentNotValidException) e)
                    .getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining("；"))
                : "参数校验失败";

        log.warn("参数校验失败：{}",msg);
        return Result.fail(ResultStatus.PARAMS_INVALID);
    }

    /**
     * 方法不支持（405）
     * @param e 不支持异常
     * @return 自定义
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result<?> handleMethodNotSupportedHandler(HttpRequestMethodNotSupportedException e) {

        log.warn("请求不支持：{}",e.getMessage());
        return Result.fail(ResultStatus.API_UN_IMPL);
    }

    /**
     * 404 找不到资源
     * @param e NoFound
     * @return 自定义
     */
    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<?> handleNoHandlerFoundException(Exception e) {

        log.warn("资源不存在：{}",e.getMessage());
        return Result.fail(ResultStatus.NOT_FOUND);
    }


    /**
     * 全局通用异常处理，兜底
     * @param e 异常类型，
     * @return 回显数据
     */
    @ExceptionHandler(value = Exception.class)
    public Result<?> exceptionHandler(Exception e) {

        log.error("发生错误，但未捕获异常",e);
        return Result.fail(ResultStatus.FAIL);
    }
}
