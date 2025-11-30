package com.itsutra.project.dto;

import com.itsutra.project.enums.SentimentType;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ResponseAnalysisResponseDTO {
    private Double technicalScore;
    private Double communicationScore;
    private Double completenessScore;
    private Double overallScore;
    private List<String> strengths;
    private List<String> improvementAreas;
    private String detailedFeedback;
    private Map<String, Double> skillScores;
    private SentimentType sentiment;
}
