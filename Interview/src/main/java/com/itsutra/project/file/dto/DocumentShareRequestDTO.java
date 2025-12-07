package com.itsutra.project.file.dto;

import com.itsutra.project.file.enums.PermissionLevel;
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
