package com.itsutra.project.file.dto;

import com.itsutra.project.file.enums.PermissionLevel;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocumentShareResponseDTO {
    private Long id;
    private Long documentId;
    private Long sharedWithUserId;
    private String sharedWithRole;
    private String sharedWithEmail;
    private PermissionLevel permissionLevel;
    private String accessCode;
    private LocalDateTime expiresAt;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private UserInfoDTO sharedBy;
    private Boolean isValid;
}
