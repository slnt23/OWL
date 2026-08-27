package xyz.nanian.owl.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

import static xyz.nanian.owl.common.security.JwtConstants.CLAIM_ROLE;
import static xyz.nanian.owl.common.security.JwtConstants.CLAIM_USER_CODE;
import static xyz.nanian.owl.common.security.JwtConstants.CLAIM_USER_EMAIL;
import static xyz.nanian.owl.common.security.JwtConstants.CLAIM_USER_ID;
import static xyz.nanian.owl.common.security.JwtConstants.CLAIM_TOKEN_VERSION;

/**
 * JWT 生成与解析组件。
 *
 * <p>使用 HMAC-SHA256（HS256）对 token 进行签名，生成时写入用户基本信息、jti 和 tokenVersion，
 * 解析时校验签名合法性并返回 Claims，供 {@link filter.JwtAuthenticationFilter} 使用。</p>
 *
 * <h3>配置项</h3>
 * <ul>
 *   <li>{@code jwt.secret} — 签名密钥，默认使用本地测试密钥（生产环境务必覆盖）</li>
 *   <li>{@code jwt.expire-time} — token 有效期（毫秒），默认 30 天（2592000000）</li>
 * </ul>
 *
 * <h3>Token 结构</h3>
 * <pre>{@code
 * {
 *   "sub": "login",
 *   "jti": "uuid",           // 唯一标识，用于撤销
 *   "userId": 1,
 *   "userCode": "xxx",
 *   "userEmail": "xxx@xx.com",
 *   "role": "ADMIN",
 *   "tv": 1,                  // token 版本号，用于批量失效
 *   "iat": 1234567890,
 *   "exp": 1234567890
 * }
 * }</pre>
 *
 * @author slnt23
 * @since 2026/8/17
 */
@Component
public class JwtTokenProvider {

    private final Key key;
    private final long expireTime;

    /**
     * 根据配置创建 JWT 提供者。
     *
     * @param secret     签名密钥，默认使用本地测试密钥
     * @param expireTime token 有效期，单位毫秒
     */
    public JwtTokenProvider(
            @Value("${jwt.secret:nanian-owl-jwt-secret-key-32bytes-test}") String secret,
            @Value("${jwt.expire-time:2592000000}") long expireTime) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expireTime = expireTime;
    }

    // [TO_BE_DELETED] 旧 token 生成逻辑，请使用带 tokenVersion 的重载。
    // public String generateToken(Long userId, String userCode, String userEmail, String roleName) {
    //     long now = System.currentTimeMillis();
    //     return Jwts.builder()
    //             .setSubject("login")
    //             .claim(CLAIM_USER_ID, userId)
    //             .claim(CLAIM_USER_CODE, userCode)
    //             .claim(CLAIM_USER_EMAIL, userEmail)
    //             .claim(CLAIM_ROLE, roleName)
    //             .setIssuedAt(new Date(now))
    //             .setExpiration(new Date(now + expireTime))
    //             .signWith(key, SignatureAlgorithm.HS256)
    //             .compact();
    // }

    /**
     * 生成带 jti 与 tokenVersion 的 JWT。
     *
     * <p>jti 用于支持登出时加入黑名单，tokenVersion 用于在修改密码、换绑邮箱等
     * 场景下使该用户此前签发的旧 token 全部失效。</p>
     *
     * @param userId       用户主键 ID
     * @param userCode     用户编码
     * @param userEmail    用户邮箱
     * @param roleName     角色名
     * @param tokenVersion 当前用户的 token 版本号
     * @return 签名后的 JWT 字符串
     */
    public String generateToken(Long userId, String userCode, String userEmail,
                                String roleName, long tokenVersion) {
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject("login")
                .claim(CLAIM_USER_ID, userId)
                .claim(CLAIM_USER_CODE, userCode)
                .claim(CLAIM_USER_EMAIL, userEmail)
                .claim(CLAIM_ROLE, roleName)
                .claim(CLAIM_TOKEN_VERSION, tokenVersion)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expireTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析 JWT 并返回 Claims。
     *
     * @param token 待解析的 JWT 字符串
     * @return 解析后的 Claims
     * @throws io.jsonwebtoken.JwtException token 非法、过期或签名错误时抛出
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}