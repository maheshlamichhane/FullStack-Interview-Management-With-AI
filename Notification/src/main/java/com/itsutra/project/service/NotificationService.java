package com.itsutra.project.service;


import com.itsutra.project.dto.NotificationRequest;
import com.itsutra.project.dto.NotificationResponse;

public interface NotificationService {
    NotificationResponse sendNotification(NotificationRequest request);
    void processPendingNotifications();
}
