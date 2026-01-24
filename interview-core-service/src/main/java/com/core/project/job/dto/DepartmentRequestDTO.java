package com.core.project.job.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DepartmentRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;
    private String code;
    private String description;
    private Long managerId;
    private boolean isActive;
    private String budgetCode;
    private String costCenter;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
