package xyz.nanian.owl.constant;


/**
 * jwt所用常量
 *
 * @author slnt23
 * @since 2026/1/20
 */

public class JwtConstant {

    /**
     * 至少 32 字节（256 bit）
     * 实际项目必须放配置文件 / 环境变量
     */
    public static final String SECRET =
            "nanian-owl-jwt-secret-key-32bytes";

    /**
     * token 有效期：30 天（毫秒）
     * 注意：这里只是“时长”，不是具体时间点
     */
    public static final long EXPIRE_TIME =
            1000L * 60 * 60 * 24 * 30;
}
