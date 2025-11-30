package com.itsutra.project.dto;

import com.itsutra.project.enums.VisualizationType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class VisualizationResponseDTO {
    private Long id;
    private String name;
    private String description;
    private VisualizationType type;
    private Map<String, Object> config;
    private String dataQuery;
    private Integer width;
    private Integer height;
    private Boolean isInteractive;
    private Integer refreshInterval;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ReportSummaryResponseDTO report;
    private DashboardSummaryResponseDTO dashboard;
    private Map<String, Object> sampleData;
}
