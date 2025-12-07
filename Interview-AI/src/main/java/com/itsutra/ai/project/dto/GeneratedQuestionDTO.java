package com.itsutra.ai.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneratedQuestionDTO {

    private UUID questionId;
    private String category;
    private String difficulty;
    private String questionText;
    private String expectedAnswer;
    private Map<String, Object> evaluationCriteria;
    private List<String> tags;
    private String modelUsed;
}