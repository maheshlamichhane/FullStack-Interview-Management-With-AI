package com.itsutra.project.candidate.dto;


import com.itsutra.project.candidate.enums.ProficiencyLevel;
import lombok.Data;

@Data
public class CandidateSkillResponseDTO {
    private Long id;
    private Long candidateId;
    private String skillName;
    private ProficiencyLevel proficiencyLevel;
    private Double yearsOfExperience;
    private Integer lastUsed;
    private Boolean isCertified;
    private String certificationName;
}
