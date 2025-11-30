package com.itsutra.project.dto;

import com.itsutra.project.enums.ReportCategory;
import com.itsutra.project.enums.ReportType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReportResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String code;
    private ReportType reportType;
    private ReportCategory category;
    private String sqlQuery;
    private String dataSource;
    private List<ReportParameterResponseDTO> parameters;
    private List<ReportColumnResponseDTO> columns;
    private Integer refreshFrequency;
    private Boolean isScheduled;
    private Boolean isPublic;
    private Boolean isActive;
    private Integer cacheDuration;
    private LocalDateTime lastRunAt;
    private LocalDateTime nextRunAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserInfoDTO createdBy;
    private Long executionCount;
    private Double averageExecutionTime;
}
