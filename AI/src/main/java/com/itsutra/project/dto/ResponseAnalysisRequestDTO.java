package com.itsutra.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class ResponseAnalysisRequestDTO {

    @NotNull
    private Long candidateId;

    @NotNull
    private Long interviewId;

    @NotBlank
    private String question;

    @NotBlank
    private String candidateResponse;

    private String context;
    private Map<String, Object> evaluationCriteria;
}
