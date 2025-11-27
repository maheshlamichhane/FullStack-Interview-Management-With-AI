package com.itsutra.project.dao;


import com.itsutra.project.entity.OTPVerification;
import com.itsutra.project.enums.OTPType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OTPDao extends JpaRepository<OTPVerification, UUID> {



    // Find latest active OTP for email
    Optional<OTPVerification> findTopByEmailAndTypeAndUsedFalseAndBlockedFalseOrderByCreatedAtDesc(
            String email, OTPType type);

    // Find by token and email
    Optional<OTPVerification> findByOtpTokenAndEmailAndType(String otpToken, String email, OTPType type);

    // Find all active OTPs for an email
    List<OTPVerification> findByEmailAndUsedFalseAndBlockedFalseAndExpiresAtAfter(
            String email, LocalDateTime now);

    // Count recent OTP attempts by IP
    @Query("SELECT COUNT(o) FROM OTPVerification o WHERE o.ipAddress = :ipAddress AND o.createdAt > :since")
    Long countRecentAttemptsByIp(@Param("ipAddress") String ipAddress, @Param("since") LocalDateTime since);

    // Count recent OTP attempts by email
    @Query("SELECT COUNT(o) FROM OTPVerification o WHERE o.email = :email AND o.createdAt > :since")
    Long countRecentAttemptsByEmail(@Param("email") String email, @Param("since") LocalDateTime since);

    // Mark all previous OTPs as used
    @Modifying
    @Query("UPDATE OTPVerification o SET o.used = true, o.usedAt = CURRENT_TIMESTAMP WHERE o.email = :email AND o.type = :type AND o.used = false")
    void invalidatePreviousOtps(@Param("email") String email, @Param("type") OTPType type);

    // Clean up expired OTPs
    @Modifying
    @Query("DELETE FROM OTPVerification o WHERE o.expiresAt < :expiryTime")
    void deleteExpiredOtps(@Param("expiryTime") LocalDateTime expiryTime);
}
