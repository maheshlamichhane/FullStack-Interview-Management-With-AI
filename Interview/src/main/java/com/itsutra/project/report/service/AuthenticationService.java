package com.itsutra.project.report.service;

import com.itsutra.project.report.dao.UserDAO;
import com.itsutra.project.report.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UserDAO userDAO;

    public User getCurrentUser() {
        return userDAO.findById(1l).get();
    }
}
