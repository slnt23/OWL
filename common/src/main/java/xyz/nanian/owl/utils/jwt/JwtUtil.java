package xyz.nanian.owl.utils.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import static xyz.nanian.owl.constant.JwtConstant.EXPIRE_TIME;
import static xyz.nanian.owl.constant.JwtConstant.SECRET;

/**
 * JWT登陆认证
 *
 * @author slnt23
 * @since 2026/1/20
 */

public class JwtUtil {

    private static final Key KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));


    public static String generateToken(@RequestParam Long userId, @RequestParam String userName) {

        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setSubject("login")
                .claim("userId",userId)
                .claim("userName",userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(now + EXPIRE_TIME))
                .signWith(KEY,SignatureAlgorithm.HS256)
                .compact();
    }


    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
