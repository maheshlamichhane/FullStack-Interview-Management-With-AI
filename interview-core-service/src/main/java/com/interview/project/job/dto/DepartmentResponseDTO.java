package com.interview.project.job.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DepartmentResponseDTO {
    private Long id;
    private String name;
    private String code;
    private String description;
    private DepartmentResponseDTO parentDepartment;
    private List<DepartmentResponseDTO> childDepartments;
    private Long managerId;
    private Boolean isActive;
    private String budgetCode;
    private String costCenter;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer jobPositionCount;
    private Integer teamSize;
}
