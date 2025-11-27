package com.itsutra.project.service;

import com.itsutra.project.dto.ResetPasswordRequest;
import com.itsutra.project.dto.SignUpRequest;
import com.itsutra.project.entity.User;
import com.itsutra.project.exception.InterviewException;

public interface AuthenticationService {

    public User signUp(SignUpRequest request);
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) throws InterviewException;
}
