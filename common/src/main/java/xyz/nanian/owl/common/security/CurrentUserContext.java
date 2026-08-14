package xyz.nanian.owl.common.security;

/**
 * Thread-local holder for the authenticated user of the current request.
 */
public final class CurrentUserContext {

    private static final ThreadLocal<LoginUser> LOGIN_USER = new ThreadLocal<>();

    private CurrentUserContext() {
    }

    public static void setLoginUser(LoginUser loginUser) {
        LOGIN_USER.set(loginUser);
    }

    public static LoginUser getLoginUser() {
        return LOGIN_USER.get();
    }

    public static String getUserCode() {
        LoginUser loginUser = getLoginUser();
        return loginUser == null ? null : loginUser.getUserCode();
    }

    public static Long getUserId() {
        LoginUser loginUser = getLoginUser();
        return loginUser == null ? null : loginUser.getUserId();
    }

    public static String getRoleName() {
        LoginUser loginUser = getLoginUser();
        return loginUser == null ? null : loginUser.getRoleName();
    }

    public static void clear() {
        LOGIN_USER.remove();
    }
}
