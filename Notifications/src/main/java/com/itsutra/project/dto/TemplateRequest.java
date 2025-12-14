package com.itsutra.project.dto;


import com.itsutra.project.enums.NotificationType;
import lombok.Data;

@Data
public class TemplateRequest {
    private String templateName;
    private String subject;
    private String body;
    private NotificationType type;
    private String language;
}
