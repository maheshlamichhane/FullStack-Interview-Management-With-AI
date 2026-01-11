package com.interview.project.entity;


import com.interview.project.enums.NotificationStatus;
import com.interview.project.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;


@Table(name = "notification_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationHistory {

    @Id
    private Long id;

    private String recipient;

    private String subject;

    private String message;

    private NotificationType type;

    private NotificationStatus status;

    private String providerResponse;

    private String referenceId;

    private LocalDateTime sentAt;

    private LocalDateTime deliveredAt;
    private LocalDateTime createdAt;

}
