package xyz.nanian.owl.common.security;

import lombok.Data;

/**
 * 当前登录用户信息。
 *
 * <p>由 JWT 认证过滤器从 token Claims 中还原，写入 {@link CurrentUserContext}，
 * 供业务代码获取当前操作用户。</p>
 */
@Data
public class LoginUser {

    /** 用户主键 ID */
    private Long userId;

    /** 用户业务编码，对外标识 */
    private String userCode;

    /** 用户邮箱 */
    private String email;

    /** 角色名，例如 ADMIN、MERCHANT、USER */
    private String roleName;
}
