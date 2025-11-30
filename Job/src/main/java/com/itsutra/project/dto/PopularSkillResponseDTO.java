package com.itsutra.project.dto;

import lombok.Data;

@Data
public class PopularSkillResponseDTO {
    private String skillName;
    private Long jobCount;
    private Double averageProficiencyLevel;
}
