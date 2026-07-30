package xyz.nanian.owl.common.exception;

import lombok.Getter;
import xyz.nanian.owl.common.result.ResultStatus;

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
