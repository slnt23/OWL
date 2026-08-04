package xyz.nanian.owl.common.security;

import lombok.Data;

/**
 * Authenticated user principal carried by the request context.
 */
@Data
public class LoginUser {

    private Long userId;
    private String userCode;
    private String email;
    private String roleName;
}
