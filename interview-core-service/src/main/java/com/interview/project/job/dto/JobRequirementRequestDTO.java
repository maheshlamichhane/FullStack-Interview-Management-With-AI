package com.interview.project.job.dto;

import com.interview.project.job.enums.RequirementType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JobRequirementRequestDTO {

    @NotBlank(message = "Requirement type is required")
    private RequirementType requirementType;

    @NotBlank(message = "Description is required")
    private String description;

    private Boolean isMandatory;
    private Integer priority;
}
