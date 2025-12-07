package com.itsutra.ai.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionGenerationResponse {

    private UUID generationId;
    private List<GeneratedQuestionDTO> questions;
    private Integer totalQuestions;
    private Map<String, Integer> difficultyDistribution;
    private Map<String, Integer> categoryDistribution;
    private LocalDateTime generatedAt;
}
