package com.itsutra.project.service;

import com.itsutra.project.dao.UserDAO;
import com.itsutra.project.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UserDAO userDAO;

    public  User getCurrentUser() {
        return userDAO.findById(1l).get();
    }
}
