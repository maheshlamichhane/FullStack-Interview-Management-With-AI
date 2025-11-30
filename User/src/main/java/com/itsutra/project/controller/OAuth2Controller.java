package com.itsutra.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/oauth2")
@RequiredArgsConstructor
public class OAuth2Controller {

    @GetMapping("/providers")
    public ResponseEntity<Map<String, String>> getOAuth2Providers() {
        Map<String, String> providers = new HashMap<>();
        providers.put("google", "/oauth2/authorization/google");
        providers.put("github", "/oauth2/authorization/github");
        // Add more providers as needed

        return ResponseEntity.ok(providers);
    }

    @GetMapping("/success")
    public ResponseEntity<Map<String, String>> oauth2Success() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "OAuth2 authentication successful");
        response.put("redirectUrl", "/dashboard"); // Redirect URL for frontend
        return ResponseEntity.ok(response);
    }

    @GetMapping("/failure")
    public ResponseEntity<Map<String, String>> oauth2Failure() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "OAuth2 authentication failed");
        response.put("redirectUrl", "/login?error=oauth2_failed");
        return ResponseEntity.ok(response);
    }
}
