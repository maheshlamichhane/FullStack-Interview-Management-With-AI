package com.itsutra.project.notification.service;


import com.itsutra.project.notification.dto.NotificationRequest;
import com.itsutra.project.notification.dto.NotificationResponse;

public interface NotificationService {

    public NotificationResponse manageHistory(NotificationRequest request);
    public String sendNotification(NotificationRequest request, String body);
}
