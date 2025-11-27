package com.itsutra.project.service;

import com.itsutra.project.dao.UserDao;
import com.itsutra.project.entity.User;
import com.itsutra.project.exception.InterviewException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    @Override
    @Transactional
    public void updateUserEmailVerifiedInfo(String email ){
       userDao.updateVerifyEmailByEmail(email);
    }

    @Override
    public User findByEmail(String email) throws InterviewException {
        return userDao.findByEmail(email)
                .orElseThrow(() -> new InterviewException("User not found with email: " + email));
    }

    @Override
    public User saveUser(User user) {
        userDao.save(user);
        return user;
    }


}
