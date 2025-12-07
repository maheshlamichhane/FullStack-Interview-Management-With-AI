package com.itsutra.ai.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewAnalysisResponse {

    private UUID analysisId;
    private UUID interviewId;
    private BigDecimal overallScore;
    private BigDecimal technicalScore;
    private BigDecimal communicationScore;
    private BigDecimal problemSolvingScore;
    private List<String> strengths;
    private List<String> weaknesses;
    private String feedbackSummary;
    private String improvementSuggestions;
    private Map<String, Object> sentimentAnalysis;
    private Map<String, Object> keywordMatches;
    private String generatedReport;
    private LocalDateTime analyzedAt;
}
