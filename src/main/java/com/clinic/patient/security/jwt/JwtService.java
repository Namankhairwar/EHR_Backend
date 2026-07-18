package com.clinic.patient.security.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private String refresh_expiration;

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);



    public String generateNewToken(String email) {
 return Jwts.builder()
                .setSubject(email)
                .claim("type", "Access")
                .setIssuedAt(new Date(System.currentTimeMillis()))
          .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)),SignatureAlgorithm.HS256)
                .compact();
         }

    public String generateRefreshToken(String email){
        logger.info("Generating the refresh Tokens for : {}",email);
        return Jwts.builder()
                .setSubject(email)
                .claim("type","Refresh")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)),SignatureAlgorithm.HS256)
                .compact();
    }



    public boolean validateToken(String token){
        try{
            logger.info("Validating the token {}",token);
            Jwts.
                    parserBuilder().
                    setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8)).
                    build().parseClaimsJws(token).getBody().getSubject();
            logger.info("Successfully validate the token ");
            return true;
        }catch(ExpiredJwtException e){
        logger.warn("Token is expired");
        }
        catch(JwtException e){
            logger.error("Token validation is failed");
        }
        return false;
    }

}
