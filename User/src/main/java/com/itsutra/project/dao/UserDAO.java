package com.itsutra.project.dao;

import com.itsutra.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDAO extends JpaRepository<User,Long> {
    public Optional<User> findByUsername(String username);
    public Optional<User> findByEmail(String email);
}
