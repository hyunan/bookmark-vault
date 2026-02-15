package com.github.hyunan.bookmarkvault.repository;

import java.util.Optional;

import com.github.hyunan.bookmarkvault.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByUsername(String username);

    void deleteUserById(Integer id);
}
