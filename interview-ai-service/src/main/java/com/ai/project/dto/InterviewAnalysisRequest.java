package com.ai.project.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewAnalysisRequest {

    @NotNull(message = "Interview ID is required")
    private UUID interviewId;

    private String interviewTranscript;
    private List<String> questions;
    private List<String> answers;
    private String jobDescription;
    private List<String> requiredSkills;

    private Map<String, Object> analysisConfig;
}
