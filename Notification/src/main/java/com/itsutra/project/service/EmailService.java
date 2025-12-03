package com.itsutra.project.service;



public interface EmailService {
    String sendEmail(String to, String subject, String body);
    String sendEmailWithAttachment(String to, String subject, String body, String attachmentPath);
}
