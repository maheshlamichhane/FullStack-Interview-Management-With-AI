package com.itsutra.project.dto;

import com.itsutra.project.enums.RequirementType;
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
