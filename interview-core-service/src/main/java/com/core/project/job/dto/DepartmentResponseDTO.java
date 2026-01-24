package com.core.project.job.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DepartmentResponseDTO {
    private Long id;
    private String name;
    private String code;
    private String description;
    private Long managerId;
    private Boolean isActive;
    private String budgetCode;
    private String costCenter;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer jobPositionCount;
    private Integer teamSize;
}
