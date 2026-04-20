package xyz.nanian.owl.exception;


import lombok.Getter;
import xyz.nanian.owl.result.ResultStatus;

/**
 * 自定义的 业务异常 Buiness Exception
 *
 * @author slnt23
 * @since 2026/1/20
 */

@Getter
public class BizException extends RuntimeException {

    private final Integer code;

    public BizException(ResultStatus status) {
        super(status.getMessage());
        this.code = status.getCode();
    }
}
