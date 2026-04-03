package xyz.nanian.owl.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 自定义的 业务异常 Buiness Exception
 *
 * @author slnt23
 * @since 2026/1/20
 */

@Getter
public class BizException extends RuntimeException {

    private final int code;
    private final String message;

    public BizException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BizException(HttpStatus status, String message) {
        super(message);
        this.code = status.value();
        this.message = message;
    }
}
