package com.github.hyunan.bookmarkvault.service;

import com.github.hyunan.bookmarkvault.entity.User;
import com.github.hyunan.bookmarkvault.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
