package xyz.nanian.owl.constant;


/**
 * Login
 *
 * @author slnt23
 * @since 2026/4/10
 */

public class LoginConstant {

//    有关验证码的，
    // Redis中验证码的key前缀,必须是这个 前缀 + 邮箱号，
    public static final String VERIFICATION_CODE_PREFIX = "verification:code:";

    // 验证码过期时间（分钟）
    public static final long CODE_EXPIRE_MINUTES = 5;

    public static final String CODE_TIME_IN_5_MIN = "5分钟内已发送验证码，请稍后再试";

    public static final String NONE_USER="用户不存在";
}
