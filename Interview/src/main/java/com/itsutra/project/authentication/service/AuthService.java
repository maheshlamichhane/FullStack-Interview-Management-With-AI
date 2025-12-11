//package com.itsutra.project.authentication.service;//package com.itsutra.project.service;
//
//import com.itsutra.project.dto.LoginRequest;
//import com.itsutra.project.dto.TokenResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//@Service
//@RequiredArgsConstructor
//public class AuthService {
//
//    @Value("${keycloak.auth-server-url}")
//    private String tokenUrl;
//
//    @Value("${keycloak.client-id}")
//    private String clientId;
//
//    @Value("${keycloak.client-secret}")
//    private String clientSecret;
//
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    public TokenResponse login(LoginRequest request) {
//
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("grant_type", "authorization_code");
//        body.add("client_id", clientId);
//        body.add("client_secret", clientSecret);
//        body.add("code","12345");
//
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        HttpEntity<MultiValueMap<String, String>> entity =
//                new HttpEntity<>(body, headers);
//
//        ResponseEntity<TokenResponse> response =
//                restTemplate.exchange(tokenUrl, HttpMethod.POST, entity, TokenResponse.class);
//
//        return response.getBody();
//    }
//}
//
