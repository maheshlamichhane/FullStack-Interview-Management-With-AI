package com.itsutra.project.dao;

import com.itsutra.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<User,Long> {
}
