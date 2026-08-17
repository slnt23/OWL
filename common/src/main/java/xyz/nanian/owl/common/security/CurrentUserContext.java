package xyz.nanian.owl.common.security;

/**
 * 当前登录用户上下文。
 *
 * <p>基于 ThreadLocal 保存当前请求的 {@link LoginUser}，
 * 由 JWT 认证过滤器在校验通过后写入，并在请求结束时统一清理。</p>
 *
 * <p>业务代码应通过 {@link #getUserId()}、{@link #getUserCode()}、
 * {@link #getRoleName()} 获取当前登录用户，避免自行解析 Token。</p>
 */
public final class CurrentUserContext {

    private static final ThreadLocal<LoginUser> LOGIN_USER = new ThreadLocal<>();

    private CurrentUserContext() {
    }

    /**
     * 将当前登录用户写入当前线程的上下文。
     *
     * @param loginUser 当前登录用户，可为 null
     */
    public static void setLoginUser(LoginUser loginUser) {
        LOGIN_USER.set(loginUser);
    }

    /**
     * 获取当前线程的登录用户。
     *
     * @return 当前登录用户；未登录或上下文不存在时返回 null
     */
    public static LoginUser getLoginUser() {
        return LOGIN_USER.get();
    }

    /**
     * 获取当前登录用户的用户编码。
     *
     * @return 用户编码；未登录时返回 null
     */
    public static String getUserCode() {
        LoginUser loginUser = getLoginUser();
        return loginUser == null ? null : loginUser.getUserCode();
    }

    /**
     * 获取当前登录用户的用户 ID。
     *
     * @return 用户主键 ID；未登录时返回 null
     */
    public static Long getUserId() {
        LoginUser loginUser = getLoginUser();
        return loginUser == null ? null : loginUser.getUserId();
    }

    /**
     * 获取当前登录用户的角色名。
     *
     * @return 角色名；未登录时返回 null
     */
    public static String getRoleName() {
        LoginUser loginUser = getLoginUser();
        return loginUser == null ? null : loginUser.getRoleName();
    }

    /**
     * 清理当前线程的登录用户上下文。
     *
     * <p>必须在请求结束时调用，避免线程池复用导致不同请求之间串号。</p>
     */
    public static void clear() {
        LOGIN_USER.remove();
    }
}
