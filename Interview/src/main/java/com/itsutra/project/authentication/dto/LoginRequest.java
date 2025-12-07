package com.itsutra.project.authentication.dto;


import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}

