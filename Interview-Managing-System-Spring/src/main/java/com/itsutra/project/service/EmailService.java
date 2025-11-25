package com.itsutra.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender; // Spring auto-configures everything!

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("${spring.mail.username}");

        mailSender.send(message);
    }

    public void sendOTPEmail(String toEmail, String otp) {
        String subject = "Your OTP Verification Code";

        String body = String.format(
                "Your OTP verification code is: %s\n\n" +
                        "This code will expire in 5 minutes.\n\n" +
                        "If you didn't request this code, please ignore this email.\n\n" +
                        "For security reasons:\n" +
                        "- Do not share this code with anyone\n" +
                        "- Maximum 5 attempts allowed\n" +
                        "- After 5 failed attempts, the OTP will be blocked\n\n" +
                        "Best regards,\n" +
                        "Security Team",
                otp
        );

        sendEmail(toEmail, subject, body);
    }
}
