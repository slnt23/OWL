package xyz.nanian.owl.exception;

import lombok.Getter;
import xyz.nanian.owl.result.ResultStatus;

/**
 * 登陆异常
 */


@Getter
public class LoginException extends RuntimeException {

    private final Integer code;

    public LoginException(ResultStatus status) {
        super(status.getMessage());
        this.code = status.getCode();
    }
}
