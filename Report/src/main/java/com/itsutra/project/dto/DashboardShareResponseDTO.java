package com.itsutra.project.dto;

import com.itsutra.project.enums.PermissionLevel;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DashboardShareResponseDTO {
    private Long id;
    private Long dashboardId;
    private Long sharedWithUserId;
    private String sharedWithRole;
    private PermissionLevel permissionLevel;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
}
