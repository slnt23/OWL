package xyz.nanian.owl.common.security;

/**
 * JWT Claims 字段名常量。
 *
 * <p>统一定义 token 中携带的用户 ID、用户编码、邮箱、角色、jti 和 token 版本号等字段名，
 * 生成与解析 token 时共用，避免字符串散落在业务代码中。</p>
 *
 * @author slnt23
 * @since 2026/8/17
 */
public final class JwtConstants {

    /** Claims 中的用户 ID 字段名 */
    public static final String CLAIM_USER_ID = "userId";

    /** Claims 中的用户编码字段名 */
    public static final String CLAIM_USER_CODE = "userCode";

    /** Claims 中的用户邮箱字段名 */
    public static final String CLAIM_USER_EMAIL = "userEmail";

    /** Claims 中的角色名字段名 */
    public static final String CLAIM_ROLE = "role";

    /** Claims 中的 jti 字段名，用于 token 撤销 */
    public static final String CLAIM_JTI = "jti";

    /** Claims 中的 token 版本号字段名，用于使旧 token 失效 */
    public static final String CLAIM_TOKEN_VERSION = "tv";

    private JwtConstants() {
    }
}