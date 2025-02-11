package com.example.Authentication.repositories;

import com.example.Authentication.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationRepo extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    User findByUsername(String username);
}
