package com.itsutra.project.service;


import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId.toLowerCase()) {
            case "google" -> new GoogleOAuth2UserInfo(attributes);
            case "github" -> new GithubOAuth2UserInfo(attributes);
            // Add more OAuth2 providers as needed
            default -> throw new IllegalArgumentException("Sorry! Login with " + registrationId + " is not supported yet.");
        };
    }
}
