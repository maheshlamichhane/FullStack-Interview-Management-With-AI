package com.itsutra.project.dto;

import com.itsutra.project.enums.ProficiencyLevel;
import lombok.Builder;
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
