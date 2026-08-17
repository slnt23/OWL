package xyz.nanian.owl.common.exception;

import lombok.Getter;
import xyz.nanian.owl.common.result.ResultStatus;

/**
 * Raised when login or registration cannot be completed.
 */
@Getter
public class LoginFailureException extends RuntimeException {

    private final Integer code;

    public LoginFailureException(ResultStatus status) {
        super(status.getMessage());
        this.code = status.getCode();
    }
}
