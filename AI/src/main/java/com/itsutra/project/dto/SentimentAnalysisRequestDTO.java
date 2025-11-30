package com.itsutra.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class SentimentAnalysisRequestDTO {


    @NotNull
    private Long interviewId;

    @NotBlank
    private String transcript;

    private List<String> focusAreas;
}
