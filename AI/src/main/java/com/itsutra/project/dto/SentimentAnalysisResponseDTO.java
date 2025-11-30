package com.itsutra.project.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SentimentAnalysisResponseDTO {
    private Double overallSentiment;
    private Map<String, Double> aspectSentiments;
    private String dominantEmotion;
    private List<String> keyPhrases;
    private List<SentimentInsight> insights;
    private Boolean requiresFollowUp;

    @Data
    public static class SentimentInsight {
        private String aspect;
        private String insight;
        private Double confidence;
        private String recommendation;
    }
}
