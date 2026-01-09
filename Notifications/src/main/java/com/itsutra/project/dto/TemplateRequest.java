package com.itsutra.project.dto;


import com.itsutra.project.enums.NotificationType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TemplateRequest {
    private String templateName;
    private String subject;
    private String body;
    private NotificationType type;
    private String language;
}
