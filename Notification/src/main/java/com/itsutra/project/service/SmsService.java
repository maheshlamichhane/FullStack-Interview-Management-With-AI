package com.itsutra.project.service;


public interface SmsService {
    String sendSms(String to, String message);
    String sendBulkSms(java.util.List<String> recipients, String message);
}
