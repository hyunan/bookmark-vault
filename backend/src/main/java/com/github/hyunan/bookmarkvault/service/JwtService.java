package com.github.hyunan.bookmarkvault.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration}")
    private long jwtExpiration;

    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Long getJwtExpiration() {
        return jwtExpiration;
    }

    public String generateToken(String username) {
        Date expirationDate = new Date(System.currentTimeMillis() + jwtExpiration);
        return Jwts.builder()
                .subject(username)
                .expiration(expirationDate)
                .signWith(getSigningKey()).compact();
    }

    public boolean authenticateTokens(String jws) {
        try {
            Jwts.parser().verifyWith((SecretKey) getSigningKey()).build().parseSignedClaims(jws);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}

