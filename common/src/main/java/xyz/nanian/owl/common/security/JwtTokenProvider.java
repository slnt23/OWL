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
 * Creates and parses the application JWT.
 */
@Component
public class JwtTokenProvider {

    private final Key key;
    private final long expireTime;

    public JwtTokenProvider(
            @Value("${jwt.secret:nanian-owl-jwt-secret-key-32bytes-test}") String secret,
            @Value("${jwt.expire-time:2592000000}") long expireTime) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expireTime = expireTime;
    }

    public String generateToken(Long userId, String userCode, String userEmail, String roleName) {
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setSubject("login")
                .claim(CLAIM_USER_ID, userId)
                .claim(CLAIM_USER_CODE, userCode)
                .claim(CLAIM_USER_EMAIL, userEmail)
                .claim(CLAIM_ROLE, roleName)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expireTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * [UPGRADE] token with jti and token version for revocation support.
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

    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
