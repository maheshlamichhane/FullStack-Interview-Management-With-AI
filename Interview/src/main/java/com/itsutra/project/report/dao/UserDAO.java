package com.itsutra.project.report.dao;

import com.itsutra.project.report.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserDAO extends CrudRepository<User, Long> {
//    long countByActive();
//    long countByIsActiveTrue();
}
