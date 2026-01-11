package com.interview.project.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.relational.core.mapping.Table;


@Table(name = "resume_analysis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResumeAnalysis extends BaseEntity {

    private UUID candidateId;

    private UUID resumeFileId;

    private String requestId;

    private Map<String, Object> parsedData;

    private List<String> skills;

    private Integer experienceYears;

    private String educationLevel;

    private Map<String, Object> jobTitleMatches;

    private Map<String, Object> skillGaps;

    private BigDecimal overallScore;

    private String summary;

    private Map<String, Object> extractedEntities;
}
