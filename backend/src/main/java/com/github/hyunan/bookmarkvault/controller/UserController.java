package com.github.hyunan.bookmarkvault.controller;

import com.github.hyunan.bookmarkvault.dto.TokenDTO;
import com.github.hyunan.bookmarkvault.dto.UserDTO;
import com.github.hyunan.bookmarkvault.entity.User;
import com.github.hyunan.bookmarkvault.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody UserDTO userDTO) {
        int success = userService.createUser(userDTO);
        if (success == 0)
            return ResponseEntity.ok(Map.of("success", "User " + userDTO.getUsername() + " created successfully."));
        else if (success == 1)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("failed", "username: " + userDTO.getUsername() + " already exists"));
        else if (success == 2)
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("failed", "password must be at least 4 characters long"));
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("failed", "something went wrong, please try again later"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDTO userDTO) {
        TokenDTO tokenDTO = userService.loginUser(userDTO.getUsername(), userDTO.getPassword());
        if (tokenDTO == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of("error", "username or password is incorrect")
            );
        return ResponseEntity.ok(Map.of("username", userDTO.getUsername(),
                "token", tokenDTO.getJwtToken(), "expiresIn:", tokenDTO.getJwtExpiration()));
    }
}
