package xyz.nanian.owl.common.security;

/**
 * JWT claim names used by token generation and parsing.
 */
public final class JwtConstants {

    public static final String CLAIM_USER_ID = "userId";
    public static final String CLAIM_USER_CODE = "userCode";
    public static final String CLAIM_USER_EMAIL = "userEmail";
    public static final String CLAIM_ROLE = "role";
    public static final String CLAIM_JTI = "jti";
    public static final String CLAIM_TOKEN_VERSION = "tv";

    private JwtConstants() {
    }
}
