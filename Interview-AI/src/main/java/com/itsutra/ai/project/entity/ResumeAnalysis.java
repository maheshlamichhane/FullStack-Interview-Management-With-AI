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
@Table(name = "resume_analysis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResumeAnalysis extends BaseEntity {

    @Column(name = "candidate_id", nullable = false)
    private UUID candidateId;

    @Column(name = "resume_file_id", nullable = false)
    private UUID resumeFileId;

    @Column(name = "request_id")
    private String requestId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "parsed_data", columnDefinition = "jsonb")
    private Map<String, Object> parsedData;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "skills", columnDefinition = "text[]")
    private List<String> skills;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "education_level")
    private String educationLevel;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "job_title_matches", columnDefinition = "jsonb")
    private Map<String, Object> jobTitleMatches;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "skill_gaps", columnDefinition = "jsonb")
    private Map<String, Object> skillGaps;

    @Column(name = "overall_score", precision = 5, scale = 2)
    private BigDecimal overallScore;

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "extracted_entities", columnDefinition = "jsonb")
    private Map<String, Object> extractedEntities;
}
