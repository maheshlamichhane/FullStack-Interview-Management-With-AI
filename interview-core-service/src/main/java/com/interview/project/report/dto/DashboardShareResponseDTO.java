package com.interview.project.report.dto;

import com.interview.project.report.enums.PermissionLevel;
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
