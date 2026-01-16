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
public class ResumeAnalysisRequest {

    @NotNull(message = "Candidate ID is required")
    private UUID candidateId;

    @NotNull(message = "Resume file ID is required")
    private UUID resumeFileId;

    private String resumeText;
    private String jobDescription;
    private List<String> requiredSkills;

    private Map<String, Object> parsingConfig;
}
