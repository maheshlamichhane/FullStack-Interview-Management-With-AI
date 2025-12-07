package com.itsutra.project.notification.dto;


import com.itsutra.project.notification.enums.NotificationType;
import lombok.Data;

@Data
public class TemplateRequest {
    private String templateName;
    private String subject;
    private String body;
    private NotificationType type;
    private String language;
}
