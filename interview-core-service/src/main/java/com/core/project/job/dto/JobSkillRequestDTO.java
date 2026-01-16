package com.core.project.job.dto;

import com.core.project.job.enums.ProficiencyLevel;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JobSkillRequestDTO {


    @NotBlank(message = "Skill name is required")
    private String skillName;

    private String category;
    private ProficiencyLevel proficiencyLevel;
    private Boolean isMandatory;
    private Double minExperienceYears;
    private Integer priority;
}
