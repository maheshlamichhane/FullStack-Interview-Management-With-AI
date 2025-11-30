package com.itsutra.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OAuth2LoginRequest {
    @NotBlank
    private String provider; // google, github, etc.

    private String redirectUri;
}
