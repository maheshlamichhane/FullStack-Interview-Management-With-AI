package com.core.project.job.dto;

import com.core.project.job.enums.ProficiencyLevel;
import lombok.Data;

@Data
public class JobSkillResponseDTO {
    private Long id;
    private Long jobPositionId;
    private String skillName;
    private String category;
    private ProficiencyLevel proficiencyLevel;
    private Boolean isMandatory;
    private Double minExperienceYears;
    private Integer priority;
}
