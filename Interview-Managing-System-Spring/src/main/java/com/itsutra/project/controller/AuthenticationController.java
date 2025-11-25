package com.itsutra.project.controller;

import com.itsutra.project.dto.SignUpRequest;
import com.itsutra.project.dto.SimpleSuccessResponse;
import com.itsutra.project.service.AuthenticationService;
import com.itsutra.project.service.AuthenticationServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authentication")
@Validated
@AllArgsConstructor
public class AuthenticationController {


    private AuthenticationService authenticationServiceImpl;

    @PostMapping("/signup")
    public ResponseEntity<SimpleSuccessResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        authenticationServiceImpl.signUp(signUpRequest);
        return new ResponseEntity<>(new SimpleSuccessResponse("Sign up successful"), HttpStatus.OK);
    }
}
