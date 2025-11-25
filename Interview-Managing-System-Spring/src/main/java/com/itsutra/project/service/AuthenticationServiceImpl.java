package com.itsutra.project.service;

import com.itsutra.project.dao.UserDao;
import com.itsutra.project.dto.SignUpRequest;
import com.itsutra.project.entity.User;
import com.itsutra.project.mapper.AuthenticationMapper;
import com.itsutra.project.utilities.Util;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {


    private AuthenticationMapper authenticationMapper;
    private UserDao userDao;
    private EmailService emailService;


    @Override
    public void signUp(SignUpRequest request) {
        User user = authenticationMapper.toEntity(request);
        String otp = Util.generate5DigitRandomString();
        emailService.sendOTPEmail(user.getEmail(),otp);
        userDao.save(user);
    }
}
