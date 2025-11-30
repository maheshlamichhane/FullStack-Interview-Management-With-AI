package com.itsutra.project.dao;

import com.itsutra.project.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserDAO extends CrudRepository<User, Long> {
    long countByActive();
    long countByIsActiveTrue();
}
