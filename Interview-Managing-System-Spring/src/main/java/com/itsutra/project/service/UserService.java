package com.itsutra.project.service;

import com.itsutra.project.entity.User;
import com.itsutra.project.exception.InterviewException;


public interface UserService {

    public void updateUserEmailVerifiedInfo(String email );
    public User findByEmail(String email) throws InterviewException;
    public User saveUser(User user);
}
