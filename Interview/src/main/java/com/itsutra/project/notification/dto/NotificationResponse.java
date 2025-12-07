package com.itsutra.project.notification.dto;

import com.itsutra.project.notification.enums.NotificationStatus;
import com.itsutra.project.notification.enums.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponse {
    private String notificationId;
    private NotificationType type;
    private String recipient;
    private NotificationStatus status;
    private String providerResponse;
    private String referenceId;
    private LocalDateTime sentAt;
    private LocalDateTime deliveredAt;
}
