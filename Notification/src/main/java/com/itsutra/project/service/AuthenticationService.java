package com.itsutra.project.service;

import com.itsutra.project.dao.UserDAO;
import com.itsutra.project.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UserDAO userDAO;

    @Transactional
    public  User getCurrentUser() {
        return userDAO.findById(2l).get();
    }
}
