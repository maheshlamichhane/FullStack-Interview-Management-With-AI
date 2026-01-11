package com.interview.project.dto;

import com.interview.project.enums.NotificationStatus;
import com.interview.project.enums.NotificationType;
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
