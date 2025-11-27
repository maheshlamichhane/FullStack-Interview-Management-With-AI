package com.itsutra.project.controller;

import com.itsutra.project.dto.ResetPasswordRequest;
import com.itsutra.project.dto.SignUpRequest;
import com.itsutra.project.dto.SimpleSuccessResponse;
import com.itsutra.project.entity.User;
import com.itsutra.project.enums.OTPType;
import com.itsutra.project.exception.InterviewException;
import com.itsutra.project.service.AuthenticationService;
import com.itsutra.project.service.OTPService;
import com.itsutra.project.service.UserService;
import com.itsutra.project.utilities.Constants;
import com.itsutra.project.utilities.PasswordUtil;
import com.itsutra.project.utilities.Util;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authentication")
@Validated
@AllArgsConstructor
public class AuthenticationController {


    private AuthenticationService authenticationServiceImpl;
private OTPService otpServiceImpl;
    private UserService userServiceImpl;
    private PasswordUtil passwordUtil;

    @PostMapping("/signup")
    public ResponseEntity<SimpleSuccessResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest, HttpServletRequest request) {
        User user = authenticationServiceImpl.signUp(signUpRequest);
        String ipAddress = Util.getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        otpServiceImpl.generateAndSendOTP(user.getEmail(), OTPType.REGISTRATION,ipAddress,userAgent);
        return new ResponseEntity<>(new SimpleSuccessResponse(Constants.SIGN_UP_SUCCESSFUL), HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public void forgotPassword(@RequestParam("email") String email,HttpServletRequest request) throws InterviewException {
        userServiceImpl.findByEmail(email);
        String ipAddress = Util.getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        otpServiceImpl.generateAndSendOTP(email,OTPType.PASSWORD_RESET,ipAddress,userAgent);
    }

    @PostMapping("/reset-password")
    public void resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) throws InterviewException {
        User user = userServiceImpl.findByEmail(resetPasswordRequest.getEmail());
        String salt = passwordUtil.generateSalt();
        user.setSalt(salt);
        String hashedPassword = passwordUtil.hashPassword(resetPasswordRequest.getPassword(), salt);
        user.setPasswordHash(hashedPassword);
        userServiceImpl.saveUser(user);
    }

}
