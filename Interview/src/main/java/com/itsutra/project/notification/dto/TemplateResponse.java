package com.itsutra.project.notification.dto;

import com.itsutra.project.notification.enums.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TemplateResponse {
    private Long id;
    private String templateName;
    private String subject;
    private String body;
    private NotificationType type;
    private String language;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
