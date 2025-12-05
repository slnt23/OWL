package xyz.nanian.owl.config.exception;


import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.nanian.owl.result.Result;
import xyz.nanian.owl.result.ResultStatus;

/**
 * 全局异常处理器
 *
 * @author slnt23
 * @since 2025/11/19
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 全局通用异常处理，
     * @param e 异常类型，
     * @return 回显数据
     */
    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(Exception e) {

        return Result.create(e.getMessage(), ResultStatus.FAIL);
    }

}
