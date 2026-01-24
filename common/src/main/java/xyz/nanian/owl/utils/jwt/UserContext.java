package xyz.nanian.owl.utils.jwt;


/**
 * 用户登陆信息,当前身份的临时存放
 *
 * @author slnt23
 * @since 2026/1/20
 */

public class UserContext {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        USER_ID.set(userId);
    }

    public static Long getUserId() {
        return USER_ID.get();
    }

    public static void clear() {
        USER_ID.remove();
    }
}
