package xyz.nanian.owl.user.constant;


/**
 * Login
 *
 * @author slnt23
 * @since 2026/4/10
 */

public class LoginConstant {

//    有关验证码的，
    // Redis中验证码的key前缀
    public static final String VERIFICATION_CODE_PREFIX = "verification:code:";

    // 验证码过期时间（分钟）
    public static final long CODE_EXPIRE_MINUTES = 5;
}
