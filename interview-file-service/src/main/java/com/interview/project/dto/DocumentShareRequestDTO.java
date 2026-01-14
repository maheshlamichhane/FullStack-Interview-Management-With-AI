package com.interview.project.dto;

import com.interview.project.enums.PermissionLevel;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocumentShareRequestDTO {
    private Long sharedWithUserId;
    private String sharedWithRole;
    private String sharedWithEmail;
    private PermissionLevel permissionLevel;
    private LocalDateTime expiresAt;
    private String accessCode;
}
