package xyz.nanian.owl.logging;


/**
 * 当前请求用户身份的临时存放处
 *
 * @author slnt23
 * @since 2026/1/15
 */

public class UserContext {

    private static final ThreadLocal<String> USER_ID=new ThreadLocal<>();

    public static void setUserId(String userId) {
        USER_ID.set(userId);
    }

    public static String getUserId() {
        return USER_ID.get();
    }

    public static void clear(){
        USER_ID.remove();
    }
}
