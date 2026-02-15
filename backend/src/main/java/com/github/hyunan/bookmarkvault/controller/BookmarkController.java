package com.github.hyunan.bookmarkvault.controller;

import com.github.hyunan.bookmarkvault.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {
    private final JwtService jwtService;

    public BookmarkController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("hello")
    public ResponseEntity<?> helloWorld(@RequestHeader(value = "Authorization") String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer "))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "user not authorized"));
        String token = bearerToken.substring("Bearer ".length());
        if (jwtService.authenticateTokens(token))
            return ResponseEntity.ok().body(Map.of("success", "user is authorized"));
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "user not authorized"));
    }
}
