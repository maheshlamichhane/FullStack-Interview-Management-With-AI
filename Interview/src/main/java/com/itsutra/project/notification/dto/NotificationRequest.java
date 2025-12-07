package com.itsutra.project.notification.dto;

import com.itsutra.project.notification.enums.NotificationType;
import lombok.Data;

import java.util.Map;

@Data
public class NotificationRequest {
    private NotificationType type;
    private String recipient;
    private String templateName;
    private Map<String, Object> templateVariables;
    private String subject;
//    private String message;
    private String language = "en";
//    private String referenceId;
}