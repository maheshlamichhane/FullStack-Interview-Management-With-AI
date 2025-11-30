package com.itsutra.project.dto;

import com.itsutra.project.entity.JobSkill;
import com.itsutra.project.enums.ProficiencyLevel;
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
