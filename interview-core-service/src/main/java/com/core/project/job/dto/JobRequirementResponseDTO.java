package com.core.project.job.dto;

import com.core.project.job.enums.RequirementType;
import lombok.Data;

@Data
public class JobRequirementResponseDTO {
    private Long id;
    private Long jobPositionId;
    private RequirementType requirementType;
    private String description;
    private Boolean isMandatory;
    private Integer priority;
}
