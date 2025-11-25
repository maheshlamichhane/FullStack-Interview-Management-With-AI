package com.itsutra.project.service;

import com.itsutra.project.enums.OTPType;
import com.itsutra.project.utilities.Util;
import org.springframework.stereotype.Service;

import com.itsutra.project.entity.OTPVerification;
import com.itsutra.project.dao.OTPDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class OTPService {

    @Autowired
    private OTPDao otpDao;

    @Autowired
    private EmailService emailService;

    @Value("${app.otp.expiry-minutes}")
    private int otpExpiryMinutes;

    @Value("${app.otp.max-attempts}")
    private int maxAttempts;

    @Value("${app.otp.max-requests-per-hour}")
    private int maxRequestsPerHour;

    @Value("${app.otp.max-requests-per-day}")
    private int maxRequestsPerDay;

    @Value("${app.otp.ip-block-threshold}")
    private int ipBlockThreshold;



    /**
     * Generate and send OTP
     */
    @Transactional
    public Map<String, Object> generateAndSendOTP(String email, OTPType type,
                                                  String ipAddress, String userAgent) {

        Map<String, Object> result = new HashMap<>();

        // Check rate limits
        String rateLimitCheck = checkRateLimits(email, ipAddress);
        if (rateLimitCheck != null) {
            result.put("success", false);
            result.put("error", rateLimitCheck);
            return result;
        }

        // Invalidate previous OTPs for this email and type
        otpDao.invalidatePreviousOtps(email, type);


        // Generate OTP
        String otp = Util.generate5DigitRandomString();


        // Create OTP record
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(otpExpiryMinutes);
        OTPVerification otpVerification = new OTPVerification(
                email, otp, expiresAt, ipAddress, userAgent, type
        );
        otpVerification.setMaxAttempts(maxAttempts);

        otpDao.save(otpVerification);

        // Send OTP via email
        try {
            emailService.sendOTPEmail(email, otp);
            result.put("success", true);
            result.put("otpId", otpVerification.getId());
            result.put("message", "OTP sent successfully");
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", "Failed to send OTP");
        }

        return result;
    }

    /**
     * Verify OTP with brute force protection
     */
    @Transactional
    public Map<String, Object> verifyOTP(String email, String otpToken, OTPType type,
                                         String ipAddress, String userAgent) {

        Map<String, Object> result = new HashMap<>();

        // Find the latest active OTP
        Optional<OTPVerification> otpOpt = otpDao
                .findTopByEmailAndTypeAndUsedFalseAndBlockedFalseOrderByCreatedAtDesc(email, type);

        if (otpOpt.isEmpty()) {
            result.put("success", false);
            result.put("error", "No active OTP found");
            result.put("code", "NO_ACTIVE_OTP");
            return result;
        }

        OTPVerification otpVerification = otpOpt.get();

        // Check if OTP is expired
        if (otpVerification.isExpired()) {
            result.put("success", false);
            result.put("error", "OTP has expired");
            result.put("code", "OTP_EXPIRED");
            return result;
        }

        // Check if OTP is blocked (too many attempts)
        if (otpVerification.getBlocked()) {
            result.put("success", false);
            result.put("error", "OTP blocked due to too many failed attempts");
            result.put("code", "OTP_BLOCKED");
            return result;
        }

        // Verify OTP token
        if (!otpVerification.getOtpToken().equals(otpToken)) {
            // Increment attempt count
            otpVerification.incrementAttempt();
            otpDao.save(otpVerification);

            int remainingAttempts = otpVerification.getMaxAttempts() - otpVerification.getAttemptCount();

            result.put("success", false);
            result.put("error", "Invalid OTP");
            result.put("code", "INVALID_OTP");
            result.put("remainingAttempts", Math.max(0, remainingAttempts));

            // Check if blocked after this attempt
            if (otpVerification.getBlocked()) {
                result.put("error", "Too many failed attempts. OTP has been blocked.");
                result.put("code", "OTP_BLOCKED");
            }

            return result;
        }

        // OTP is valid - mark as used
        otpVerification.markAsUsed();
        otpDao.save(otpVerification);

        result.put("success", true);
        result.put("message", "OTP verified successfully");
        result.put("otpId", otpVerification.getId());

        return result;
    }

    /**
     * Check rate limits for brute force protection
     */
    private String checkRateLimits(String email, String ipAddress) {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);

        // Check hourly limit for email
        Long hourlyEmailAttempts = otpDao.countRecentAttemptsByEmail(email, oneHourAgo);
        if (hourlyEmailAttempts >= maxRequestsPerHour) {
            return "Too many OTP requests. Please try again later.";
        }

        // Check daily limit for email
        Long dailyEmailAttempts = otpDao.countRecentAttemptsByEmail(email, oneDayAgo);
        if (dailyEmailAttempts >= maxRequestsPerDay) {
            return "Daily OTP limit exceeded. Please try again tomorrow.";
        }

        // Check IP-based limits
        Long hourlyIpAttempts = otpDao.countRecentAttemptsByIp(ipAddress, oneHourAgo);
        if (hourlyIpAttempts >= ipBlockThreshold) {
            return "Suspicious activity detected from your network. Please contact support.";
        }

        return null;
    }

    /**
     * Resend OTP
     */
    @Transactional
    public Map<String, Object> resendOTP(String email, OTPType type,
                                         String ipAddress, String userAgent) {
        return generateAndSendOTP(email, type, ipAddress, userAgent);
    }

    /**
     * Get OTP status
     */
    public Map<String, Object> getOTPStatus(String email, OTPType type) {
        Map<String, Object> result = new HashMap<>();

        Optional<OTPVerification> otpOpt = otpDao
                .findTopByEmailAndTypeAndUsedFalseAndBlockedFalseOrderByCreatedAtDesc(email, type);

        if (otpOpt.isPresent()) {
            OTPVerification otp = otpOpt.get();
            result.put("hasActiveOTP", true);
            result.put("expiresAt", otp.getExpiresAt());
            result.put("attemptCount", otp.getAttemptCount());
            result.put("remainingAttempts", otp.getMaxAttempts() - otp.getAttemptCount());
            result.put("blocked", otp.getBlocked());
        } else {
            result.put("hasActiveOTP", false);
        }

        return result;
    }

    /**
     * Clean up expired OTPs (call this periodically)
     */
    @Transactional
    public void cleanupExpiredOTPs() {
        LocalDateTime expiryTime = LocalDateTime.now().minusDays(1); // Keep for 1 day for audit
        otpDao.deleteExpiredOtps(expiryTime);
    }
}
