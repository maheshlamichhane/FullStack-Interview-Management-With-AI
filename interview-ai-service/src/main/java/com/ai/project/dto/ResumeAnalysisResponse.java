package com.ai.project.dto;

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
public class ResumeAnalysisResponse {

    private UUID analysisId;
    private UUID candidateId;
    private UUID resumeFileId;
    private List<String> skills;
    private Integer experienceYears;
    private String educationLevel;
    private Map<String, BigDecimal> skillMatches;
    private Map<String, List<String>> skillGaps;
    private BigDecimal overallScore;
    private String summary;
    private Map<String, Object> extractedEntities;
    private LocalDateTime analyzedAt;
}
