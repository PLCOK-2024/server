package com.plcok.common.security;

import com.plcok.user.entity.RoleType;
import com.plcok.user.entity.User;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtil {

    // 암호화할 때 필요한 비밀 키(secret key)
    @Value("${jwt.secret}")
    private String secretKey;

    // 토큰 유효시간 1일
    @Value("${jwt.expiration_time}")
    private long accessTokenValidTime;

    private JwtParser parser;

    // secretKey 객체 초기화, Base64로 인코딩
    @PostConstruct
    protected void init() {
        parser = Jwts.parser().setSigningKey(secretKey.getBytes());
    }

    public String createToken(Long id, RoleType role, LocalDateTime createdAt) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(id));
        claims.put("role", role.name());
        claims.put("createdAt", String.valueOf(createdAt));
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(accessTokenValidTime);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        var claims = parser.parseClaimsJws(token).getBody();
        var user = User.builder()
                .id(Long.parseLong(claims.getSubject()))
                .role(RoleType.valueOf(claims.get("role").toString()))
                .build();
        return new UsernamePasswordAuthenticationToken(user , "", user.getAuthorities());
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return getAuthentication(bearerToken.substring(7));
        }
        return null;
    }
}