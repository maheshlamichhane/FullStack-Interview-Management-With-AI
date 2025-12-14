package com.itsutra.project.service;


import com.itsutra.project.dto.NotificationRequest;
import com.itsutra.project.dto.NotificationResponse;

public interface NotificationService {

    public NotificationResponse manageHistory(NotificationRequest request);
    public String sendNotification(NotificationRequest request, String body);
}
