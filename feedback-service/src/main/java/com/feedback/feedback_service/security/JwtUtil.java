package com.feedback.feedback_service.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final String jwtSecret = "superSecureLongSecretKeyForJWT-ChangeThisToYourSecret!"; // Use env variable in prod
    private final long jwtExpirationMs = 3600_000; // 1 hour

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(String clientId, String email) {
        return Jwts.builder()
                .setSubject(clientId)
                .claim("email", email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> validateAndParse(String token) throws JwtException {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
    }

    public String getClientId(String token) {
        return validateAndParse(token).getBody().getSubject();
    }

    public Date getExpiryDate(String jwt){
        Claims claims=validateAndParse(jwt).getBody();
        return claims.getExpiration();
    }
}
