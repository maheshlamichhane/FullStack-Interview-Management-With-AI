package com.interview.project.candidate.dto;

import com.interview.project.candidate.enums.ProficiencyLevel;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CandidateSkillRequestDTO {
    @NotBlank(message = "Skill name is required")
    private String skillName;

    private ProficiencyLevel proficiencyLevel;
    private Double yearsOfExperience;
    private Integer lastUsed;
    private Boolean isCertified;
    private String certificationName;
}
