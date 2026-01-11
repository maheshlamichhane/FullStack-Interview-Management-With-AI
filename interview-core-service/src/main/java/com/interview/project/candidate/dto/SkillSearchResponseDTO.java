package com.interview.project.candidate.dto;

import lombok.Data;

@Data
public class SkillSearchResponseDTO {
    private String skillName;
    private Long candidateCount;
    private Double averageExperience;
}
