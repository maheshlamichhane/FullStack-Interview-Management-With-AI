package com.itsutra.project.report.dto;


import com.itsutra.project.report.enums.PermissionLevel;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DashboardShareRequestDTO {
    private Long sharedWithUserId;
    private String sharedWithRole;
    private PermissionLevel permissionLevel;
    private LocalDateTime expiresAt;
}
