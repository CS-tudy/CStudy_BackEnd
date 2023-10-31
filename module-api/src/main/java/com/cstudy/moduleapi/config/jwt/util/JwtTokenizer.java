package com.cstudy.moduleapi.config.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Component
public class JwtTokenizer {

    private final byte[] accessSecret;
    private final byte[] refreshSecret;

    public final static Long ACCESS_TOKEN_EXPIRE_COUNT = 30 * 60 * 1000L; // 30 minutes
    public final static Long REFRESH_TOKEN_EXPIRE_COUNT = 7 * 24 * 60 * 60 * 1000L; // 7 days

    public JwtTokenizer(@Value("${jwt.secretKey}") String accessSecret, @Value("${jwt.refreshKey}") String refreshSecret) {
        this.accessSecret = accessSecret.getBytes(UTF_8);
        this.refreshSecret = refreshSecret.getBytes(UTF_8);
    }


    public String createAccessToken(Long id, String email, List<String> roles) {
        return createToken(id, email, roles, ACCESS_TOKEN_EXPIRE_COUNT, accessSecret);
    }


    public String createRefreshToken(Long id, String email, List<String> roles) {
        return createToken(id, email, roles, REFRESH_TOKEN_EXPIRE_COUNT, refreshSecret);
    }


    public String createToken(Long id, String email, List<String> roles,
                              Long expire, byte[] secretKey) {
        Claims claims = Jwts.claims().setSubject(email);

        claims.put("memberId", id);
        claims.put("roles", roles);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expire))
                .signWith(getSigningKey(secretKey))
                .compact();
    }


    public Long getMemberIdFromToken(String token) {
        Claims claims = parseToken(token, accessSecret);
        return Long.valueOf((Integer) claims.get("memberId"));
    }
    public String getMemberEmailFromToken(String token) {
        Claims claims = parseToken(token, accessSecret);
        return String.valueOf(claims.get("sub"));
    }

    public Claims parseAccessToken(String accessToken) {
        return parseToken(accessToken, accessSecret);
    }

    public Claims parseRefreshToken(String refreshToken) {
        return parseToken(refreshToken, refreshSecret);
    }


    public Claims parseToken(String token, byte[] secretKey) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String createTokenWithDate(Long id, String email, List<String> roles, Long expire, byte[] secretKey, Date date) {
        Claims claims = Jwts.claims().setSubject(email);

        claims.put("memberId", id);
        claims.put("roles", roles);

        Date expirationDate = new Date(date.getTime() + expire);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(expirationDate)
                .signWith(getSigningKey(secretKey))
                .compact();
    }


    public static Key getSigningKey(byte[] secretKey) {
        return Keys.hmacShaKeyFor(secretKey);
    }
}
