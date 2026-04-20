package xyz.nanian.owl.utils.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import static xyz.nanian.owl.constant.JwtConstant.JWT_EXPIRE_TIME;
import static xyz.nanian.owl.constant.JwtConstant.JWT_SECRET;

/**
 * JWT登陆认证,
 *
 * 目前是只有一个    Access token  有效期短，但是暂时设置的长
 * 另一个          Refresh token 有效期长，暂时未设置
 *
 * @author slnt23
 * @since 2026/1/20
 */

public class JwtUtil {

    private static final Key KEY = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));


    /**
     * 生成token,这里改为通过id + code + email，原先是id+name
     * code:用户编号
     * email: 邮箱
     * @param userId
     * @param userCode
     * @param userEmail
     * @return
     */
    public static String generateToken(Long userId,
                                       String userCode,
                                       String userEmail) {

        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setSubject("login")
                .claim("userId", userId)
                .claim("userCode",userCode)
                .claim("userEmail",userEmail)
                .setIssuedAt(new Date())
                .setExpiration(new Date(now + JWT_EXPIRE_TIME))
                .signWith(KEY,SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析token
     * @param token
     * @return
     */
    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
