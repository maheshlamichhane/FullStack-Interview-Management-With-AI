package com.itsutra.ai.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SentimentAnalysisRequest {

    @NotBlank(message = "Text is required for sentiment analysis")
    private String text;

    private String context;
    private Map<String, Object> analysisParams;
}
