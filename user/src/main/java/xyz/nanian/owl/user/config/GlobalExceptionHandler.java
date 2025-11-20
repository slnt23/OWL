package xyz.nanian.owl.user.config;


import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.nanian.owl.result.Result;
import xyz.nanian.owl.result.ResultStatus;

/**
 * 全局异常处理器
 * TODO 这里通用的会导致springdoc的json识别不到，不不能用通用的，要用指定的，但是还是得找方法,只要这个类不注释，就会提示严重的版本不匹配问题
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
