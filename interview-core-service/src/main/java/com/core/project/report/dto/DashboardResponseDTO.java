package com.core.project.report.dto;

import com.core.project.report.enums.DashboardCategory;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class DashboardResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String code;
    private DashboardCategory category;
    private Map<String, Object> layoutConfig;
    private Boolean isPublic;
    private Boolean isActive;
    private Integer refreshInterval;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserInfoDTO createdBy;
    private List<DashboardWidgetResponseDTO> widgets;
    private List<DashboardShareResponseDTO> shares;
    private Integer reportCount;
}
