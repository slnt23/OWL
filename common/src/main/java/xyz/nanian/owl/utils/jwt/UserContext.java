package xyz.nanian.owl.utils.jwt;


import lombok.Data;

/**
 * 用户登陆信息,当前身份的临时存放
 *
 * @author slnt23
 * @since 2026/1/20
 */

public class UserContext {

    private static final ThreadLocal<UserInfo> USER_INFO = new ThreadLocal<>();

    public static void setUserInfo(UserInfo userInfo) {
        USER_INFO.set(userInfo);
    }

    public static UserInfo getUserInfo() {
        return USER_INFO.get();
    }

    public static String getUserCode(){
        return USER_INFO.get().userCode;
    }

    public static Long getUserId(){
        return USER_INFO.get().userId;
    }

    public static void clear() {
        USER_INFO.remove();
    }
}

