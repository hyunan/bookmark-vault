package com.github.hyunan.bookmarkvault.dto;

import com.github.hyunan.bookmarkvault.entity.User;

import java.util.Optional;

public class TokenDTO {
    private User user;
    private String jwtToken;
    private Long jwtExpiration;

    public TokenDTO(User user, String jwtToken, Long jwtExpiration) {
        this.user = user;
        this.jwtToken = jwtToken;
        this.jwtExpiration = jwtExpiration;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public Long getJwtExpiration() {
        return jwtExpiration;
    }

    public void setJwtExpiration(Long jwtExpiration) {
        this.jwtExpiration = jwtExpiration;
    }
}
