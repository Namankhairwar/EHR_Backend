package com.clinic.patient.security;



import com.clinic.patient.entities.User;
import com.clinic.patient.models.UserRequestDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * @author Krishana dubey
 */
@Slf4j
@Service
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
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256,)
                .compact();
    }


    public String generateRefreshToken(String email){
        logger.info("Generating the refresh Tokens for : {}",email);
        return Jwts.builder()
                .setSubject(email)
                .claim("type","Refresh")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(SignatureAlgorithm.HS256,secretKey.getBytes(StandardCharsets.UTF_8))
                .compact();
    }



    public boolean validateToken(String token){
        try{
            String email = getEmail(token);
            logger.info("Validating the token {}",email);
            Jwts.
                    parser().
                    setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8)).
                    parseClaimsJwt(token);
            logger.info("Successfully validate the token {}",email);
            return true;
        }catch(ExpiredJwtException e){
        logger.warn("Token is expired");
        }
        catch(JwtException e){
            logger.error("Token validation is failed");
        }
        return false;
    }


    public String getEmail(String token){
        return getAllClaims(token).getSubject();
    }

    public Claims getAllClaims(String token){
       return Jwts.parser().setSigningKey(secretKey).parseClaimsJwt(token).getBody();
    }

}
