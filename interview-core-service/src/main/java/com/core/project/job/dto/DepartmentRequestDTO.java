package com.core.project.job.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DepartmentRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    private String code;
    private String description;
    private Long parentDepartmentId;
    private Long managerId;
    private String budgetCode;
    private String costCenter;
}
