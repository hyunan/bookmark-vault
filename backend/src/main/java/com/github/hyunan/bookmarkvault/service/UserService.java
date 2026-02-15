package com.github.hyunan.bookmarkvault.service;

import com.github.hyunan.bookmarkvault.dto.TokenDTO;
import com.github.hyunan.bookmarkvault.dto.UserDTO;
import com.github.hyunan.bookmarkvault.entity.User;
import com.github.hyunan.bookmarkvault.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public TokenDTO loginUser(String username, String password) {
        Optional<User> optionalUser = userRepository.findUserByUsername(username);
        if (optionalUser.isEmpty())
            return null;

        User user = optionalUser.get();
        if (passwordEncoder.matches(password, user.getPassword())) {
            String jws = jwtService.generateToken(user.getUsername());
            return new TokenDTO(user, jws, jwtService.getJwtExpiration());
        }
        return null;
    }

    public int createUser(UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findUserByUsername(userDTO.getUsername());
        if (optionalUser.isPresent())
            return 1;

        if (userDTO.getPassword().length() < 4)
            return 2;

        String username = userDTO.getUsername();
        String hashedPassword = passwordEncoder.encode(userDTO.getPassword());
        User newUser = new User(username, hashedPassword);
        userRepository.save(newUser);
        return 0;
    }

    @Transactional
    public void deleteUser(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.deleteUserById(user.getId());
    }
}
