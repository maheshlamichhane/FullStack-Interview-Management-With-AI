package com.itsutra.project.dto;

import lombok.Data;

import java.util.List;

@Data
public class SkillMatchingResponseDTO {

    private Double matchPercentage;
    private List<MatchedSkillDTO> matchedSkills;
    private List<String> missingSkills;
    private List<String> recommendedSkills;
    private String fitAnalysis;
    private List<TrainingRecommendationDTO> trainingRecommendations;

    @Data
    public static class MatchedSkillDTO {
        private String skill;
        private Double confidence;
        private String level; // BEGINNER, INTERMEDIATE, EXPERT
    }

    @Data
    public static class TrainingRecommendationDTO {
        private String skill;
        private String recommendation;
        private String resources;
        private Integer estimatedTime; // in hours
    }
}