package com.github.hyunan.bookmarkvault.service;

import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final JwtService jwtService;

    public AuthService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public boolean isAuthorized(String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer "))
            return false;

        String token = bearerToken.substring("Bearer ".length());
        return jwtService.authenticateTokens(token);
    }
}
