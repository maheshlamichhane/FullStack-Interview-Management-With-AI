package com.itsutra.project.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnalysisResultDTO {


    private Long id;
    private Long candidateId;
    private Long interviewId;
    private String analysisType;
    private Object results;
    private Double sentimentScore;
    private Double confidenceLevel;
    private String keyInsights;
    private LocalDateTime createdAt;
}
