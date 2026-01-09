package com.itsutra.project.entity;

import com.itsutra.project.enums.NotificationType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;


@Table(name = "notification_template")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTemplate {

    @Id
    private Long id;

    @NotNull
    private String templateName;

    private String subject;


    private String body;

    private NotificationType type;

    private String language;

    private boolean active = true;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private long createdById;
}
