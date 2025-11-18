package com.github.postproject.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${jwt.secret-key-source}")
    private String secretKeySource;
    private String secretKey;

    @PostConstruct
    public void setUp() {
        secretKey = Base64.getEncoder().encodeToString(secretKeySource.getBytes(StandardCharsets.UTF_8));
    }

    // 토큰 유효 기간 (1시간)
    private long tokenValidMilliseconds = 1000L * 60 * 60;

    // 사용자 조회 서비스
    private final UserDetailsService userDetailsService;

    // 시크릿 생성
    public SecretKey getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // jwt 토큰 생성
    public String createToken(String email, List<String> roles) {
        // Role 정보
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);

        // 토큰 유효 기간
        Date now = new Date();
        Date expiration = new Date(now.getTime() + tokenValidMilliseconds);

        return Jwts.builder()
                .subject(email)
                .claims(claims)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    // request 헤더에서 토큰 정보를 가져오기
    public String resolveToken(HttpServletRequest req) {
        return req.getHeader("Authorization");
    }

    // jwt 토큰에서 토큰의 유효성 검사하기
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            // 만료 시간이 지금보다 미래면 통과
            Date now = new Date();
            return claims.getExpiration().after(now);
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰에서 이메일 가져오기
    private String getUserEmail(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // 토큰에서 인증 정보 가져오기
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

}
