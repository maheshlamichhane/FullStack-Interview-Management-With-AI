package com.itsutra.project.service;

import com.itsutra.project.config.JwtTokenProvider;
import com.itsutra.project.dao.UserDAO;
import com.itsutra.project.dto.AuthResponse;
import com.itsutra.project.dto.LoginRequest;
import com.itsutra.project.dto.UserPrincipal;
import com.itsutra.project.entity.User;
import com.itsutra.project.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserDAO userDAO;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Override
    @Transactional
    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        log.info("Attempting to authenticate user: {}", loginRequest.getUsernameOrEmail());

        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsernameOrEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token
            String jwt = tokenProvider.generateToken(authentication);

            // Get user principal
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            // Update last login time
            updateLastLogin(userPrincipal.getId());

            // Build response
            return buildAuthResponse(jwt, userPrincipal);

        } catch (Exception e) {
            log.error("Authentication failed for user: {}", loginRequest.getUsernameOrEmail(), e);
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void initiatePasswordReset(String email) {
        log.info("Initiating password reset for email: {}", email);

        User user = userDAO.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        // Invalidate any existing tokens
        passwordResetTokenRepository.findByUserAndExpiryDateAfter(user, LocalDateTime.now())
                .ifPresent(existingToken -> {
                    existingToken.setUsed(true);
                    passwordResetTokenRepository.save(existingToken);
                });

        // Create new reset token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(24)) // 24 hours expiry
                .used(false)
                .build();

        passwordResetTokenRepository.save(resetToken);

        // Send reset email
        sendPasswordResetEmail(user.getEmail(), token);

        log.info("Password reset token generated for user: {}", user.getEmail());
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        log.info("Attempting password reset with token");

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid password reset token"));

        // Validate token
        if (resetToken.getUsed()) {
            throw new InvalidTokenException("Password reset token has already been used");
        }

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Password reset token has expired");
        }

        // Update user password
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Mark token as used
        resetToken.setUsed(true);
        resetToken.setUsedDate(LocalDateTime.now());
        passwordResetTokenRepository.save(resetToken);

        log.info("Password successfully reset for user: {}", user.getEmail());
    }

    @Override
    @Transactional
    public TokenRefreshResponse refreshToken(String refreshToken) {
        log.info("Refreshing token");

        // Validate refresh token (you might want to store refresh tokens in database)
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new InvalidTokenException("Invalid refresh token");
        }

        String username = tokenProvider.getUsernameFromToken(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        // Create new authentication and generate new access token
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities()
        );

        String newAccessToken = tokenProvider.generateToken(authentication);

        // Optionally generate new refresh token
        String newRefreshToken = tokenProvider.generateRefreshToken(userPrincipal);

        return TokenRefreshResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .build();
    }

    @Override
    @Transactional
    public void logoutUser(String token) {
        log.info("Logging out user");

        // In JWT, we typically handle logout on client side by removing the token
        // For server-side logout, you might want to implement a token blacklist
        // This is a simple implementation - you might want to enhance it

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // Optional: Add token to blacklist (if implementing blacklist)
        // tokenBlacklistService.addToBlacklist(token);

        SecurityContextHolder.clearContext();
        log.info("User logged out successfully");
    }

    @Override
    public boolean validateToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return tokenProvider.validateToken(token);
    }

    // Helper Methods
    private void updateLastLogin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }

    private AuthResponse buildAuthResponse(String jwt, UserPrincipal userPrincipal) {
        return AuthResponse.builder()
                .token(jwt)
                .type("Bearer")
                .id(userPrincipal.getId())
                .username(userPrincipal.getUsername())
                .email(userPrincipal.getEmail())
                .roles(userPrincipal.getAuthorities().stream()
                        .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                        .collect(java.util.stream.Collectors.toSet()))
                .build();
    }

    private void sendPasswordResetEmail(String email, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject("Password Reset Request - Interview Management System");

            String resetUrl = "http://localhost:3000/reset-password?token=" + token;
            String emailContent = buildPasswordResetEmailContent(resetUrl);

            helper.setText(emailContent, true); // true indicates HTML content

            mailSender.send(message);
            log.info("Password reset email sent to: {}", email);

        } catch (MessagingException e) {
            log.error("Failed to send password reset email to: {}", email, e);
            throw new RuntimeException("Failed to send password reset email");
        }
    }

    private String buildPasswordResetEmailContent(String resetUrl) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .button { background-color: #007bff; color: white; padding: 12px 24px; 
                             text-decoration: none; border-radius: 4px; display: inline-block; }
                    .footer { margin-top: 20px; font-size: 12px; color: #666; }
                </style>
            </head>
            <body>
                <div class="container">
                    <h2>Password Reset Request</h2>
                    <p>You have requested to reset your password for the Interview Management System.</p>
                    <p>Click the button below to reset your password:</p>
                    <p>
                        <a href="%s" class="button">Reset Password</a>
                    </p>
                    <p>If the button doesn't work, copy and paste this link into your browser:</p>
                    <p>%s</p>
                    <p>This link will expire in 24 hours.</p>
                    <div class="footer">
                        <p>If you didn't request this reset, please ignore this email.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(resetUrl, resetUrl);
    }
}
