package com.itsutra.project.dto;

import lombok.Data;

@Data
public class SkillSearchResponseDTO {
    private String skillName;
    private Long candidateCount;
    private Double averageExperience;
}
