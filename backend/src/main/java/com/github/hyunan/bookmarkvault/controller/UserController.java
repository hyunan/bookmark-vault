package com.github.hyunan.bookmarkvault.controller;

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

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody(required = true)UserDTO userDTO) {
        Optional<User> user = userService.getUserByUsername(userDTO.getUsername());
        if (user.isPresent())
            return ResponseEntity.ok(Map.of("msg", userDTO.getPassword(), "msg2", user.get().getPassword()));
        String errorMsg = "user: " + userDTO.getUsername() + " not found";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("msg", errorMsg));
    }

    @GetMapping("hello")
    public String helloWorld() {
        return "Hello World";
    }
}
