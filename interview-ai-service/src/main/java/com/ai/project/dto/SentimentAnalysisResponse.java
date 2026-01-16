package com.ai.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SentimentAnalysisResponse {

    private String text;
    private String sentiment; // POSITIVE, NEGATIVE, NEUTRAL
    private BigDecimal confidence;
    private Map<String, BigDecimal> emotionScores;
    private List<String> keyPhrases;
    private Map<String, Object> detailedAnalysis;
}
