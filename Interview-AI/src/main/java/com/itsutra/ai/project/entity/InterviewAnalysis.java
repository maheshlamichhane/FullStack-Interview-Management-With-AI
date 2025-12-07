package com.itsutra.ai.project.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "interview_analysis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewAnalysis extends BaseEntity {

    @Column(name = "interview_id", nullable = false)
    private UUID interviewId;

    @Column(name = "request_id")
    private String requestId;

    @Column(name = "overall_score", precision = 5, scale = 2)
    private BigDecimal overallScore;

    @Column(name = "technical_score", precision = 5, scale = 2)
    private BigDecimal technicalScore;

    @Column(name = "communication_score", precision = 5, scale = 2)
    private BigDecimal communicationScore;

    @Column(name = "problem_solving_score", precision = 5, scale = 2)
    private BigDecimal problemSolvingScore;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "strengths", columnDefinition = "text[]")
    private List<String> strengths;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "weaknesses", columnDefinition = "text[]")
    private List<String> weaknesses;

    @Column(name = "feedback_summary", columnDefinition = "TEXT")
    private String feedbackSummary;

    @Column(name = "improvement_suggestions", columnDefinition = "TEXT")
    private String improvementSuggestions;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "sentiment_analysis", columnDefinition = "jsonb")
    private Map<String, Object> sentimentAnalysis;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "keyword_matches", columnDefinition = "jsonb")
    private Map<String, Object> keywordMatches;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "transcript_analysis", columnDefinition = "jsonb")
    private Map<String, Object> transcriptAnalysis;

    @Column(name = "generated_report", columnDefinition = "TEXT")
    private String generatedReport;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "analysis_metadata", columnDefinition = "jsonb")
    private Map<String, Object> analysisMetadata;
}
