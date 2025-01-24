package org.example.expert.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_EXP = 30 * 60 * 1000L; // 30분
    private static final long REFRESH_TOKEN_EXP = 12 * 60 * 60 * 1000L; // 12시간

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String generateAccessToken(Long id, String email, String nickname, UserRole role) {
        Date now = new Date();

        return BEARER_PREFIX + Jwts.builder()
                .claim("id", id.toString())
                .claim("email", email)
                .claim("nickname", nickname)
                .claim("role", role.name())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXP))
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public String generateRefreshToken(Long id) {
        Date now = new Date();

        return Jwts.builder()
                .claim("id", id.toString())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXP))
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isValid(String token) {
        try {
            JwtParser jwtParser = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build();

            Claims claims = jwtParser.parseClaimsJws(token).getBody();
            return claims.getExpiration().after(new Date());
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.warn("지원되지 않는 JWT 토큰 입니다.");
        } catch (Exception e) {
            log.warn("유효하지 않는 JWT 토큰 입니다.");
        }

        return false;
    }

    public boolean isStartsWithBearer(String token) {
        return token.startsWith(BEARER_PREFIX);
    }

    public User getUserFromToken(String token) {
        Claims claims = extractClaims(token);

        return User.builder()
                .id(Long.valueOf(claims.get("id", String.class)))
                .email(claims.get("email", String.class))
                .nickname(claims.get("nickname", String.class))
                .userRole(UserRole.of(claims.get("role", String.class)))
                .build();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = extractClaims(token);

        return Long.valueOf(claims.get("id", String.class));
    }
}
