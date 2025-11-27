package com.itsutra.project.service;

import com.itsutra.project.dto.ResetPasswordRequest;
import com.itsutra.project.dto.SignUpRequest;
import com.itsutra.project.entity.User;
import com.itsutra.project.exception.InterviewException;
import com.itsutra.project.mapper.AuthenticationMapper;
import com.itsutra.project.utilities.PasswordUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {


    private AuthenticationMapper authenticationMapper;
    private PasswordUtil passwordUtil;
    private UserService userService;


    @Override
    @Transactional
    public User signUp(SignUpRequest request) {
        User user = authenticationMapper.toEntity(request);
        return userService.saveUser(user);
    }

    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) throws InterviewException {
        String salt = passwordUtil.generateSalt();
        String password = passwordUtil.hashPassword(resetPasswordRequest.getPassword(), salt);
        User user = userService.findByEmail(resetPasswordRequest.getEmail());
        user.setSalt(salt);
        user.setPasswordHash(password);
        userService.saveUser(user);
        System.out.println("Password has been reset");
    }
}
