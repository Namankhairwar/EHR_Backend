package com.clinic.patient.security.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author Krishana dubey
 */
@Slf4j
@Service
@NoArgsConstructor(force = true)
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private long expiration;
    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    public String generateNewToken(String email) {
        return buildToken(email, "Access", expiration);
    }

    public String generateRefreshToken(String email) {
        return buildToken(email, "Refresh", refreshExpiration);
    }

    private String buildToken(String email, String type, long validity) {
        return Jwts.builder()
                .setSubject(email)
                .claim("type", type)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Returns the email (subject) of a valid, unexpired token of the expected
     * type ("Access" or "Refresh"), or null when the token is invalid.
     */
    public String extractEmail(String token, String expectedType) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                    .build().parseClaimsJws(token).getBody();
            if (!expectedType.equals(claims.get("type", String.class))) {
                log.warn("Token type mismatch");
                return null;
            }
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            log.warn("Token is expired");
        } catch (JwtException e) {
            log.warn("Token validation failed");
        }
        return null;
    }

}
