package com.itsutra.project.service;

import com.itsutra.project.dto.*;

public interface AuthService {
    AuthResponse authenticateUser(LoginRequest loginRequest);
    void initiatePasswordReset(String email);
    void resetPassword(String token, String newPassword);
    TokenRefreshResponse refreshToken(String refreshToken);
    void logoutUser(String token);
    boolean validateToken(String token);
}
