package xyz.nanian.owl.common.security;

/**
 * 角色常量。
 *
 * <p>定义 Spring Security 权限中的角色名与权限前缀，
 * 统一用于 SecurityConfig 的角色鉴权和 JWT 认证过滤器中的权限字符串拼接。</p>
 */
public final class RoleConstants {

    /** Spring Security 权限前缀，拼接后形如 ROLE_ADMIN */
    public static final String ROLE_PREFIX = "ROLE_";

    /** 管理员角色名 */
    public static final String ADMIN = "ADMIN";

    /** 商家角色名 */
    public static final String MERCHANT = "MERCHANT";

    /** 普通用户角色名 */
    public static final String USER = "USER";

    private RoleConstants() {
    }
}
