package com.ai.project.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.relational.core.mapping.Table;

@Table(name = "interview_analysis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewAnalysis extends BaseEntity {

    private UUID interviewId;

    private String requestId;

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

    private Map<String, Object> transcriptAnalysis;

    private String generatedReport;

    private Map<String, Object> analysisMetadata;
}
