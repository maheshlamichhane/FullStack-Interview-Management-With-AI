package com.itsutra.project.dto;

import com.itsutra.project.entity.ReportExecution;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ReportExecutionResponseDTO {
    private Long id;
    private Long reportId;
    private LocalDateTime executedAt;
    private LocalDateTime completedAt;
    private ReportExecution.ExecutionStatus status;
    private Long executionTimeMs;
    private Long recordCount;
    private String errorMessage;
    private Map<String, Object> parameters;
    private String resultUrl;
}
