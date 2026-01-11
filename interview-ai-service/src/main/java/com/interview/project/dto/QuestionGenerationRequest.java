package com.interview.project.dto;

import com.interview.project.enums.QuestionDifficulty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionGenerationRequest {

    private UUID jobId;

    @NotBlank(message = "Job description is required")
    private String jobDescription;

    private List<String> requiredSkills;
    private List<QuestionDifficulty> difficulties;
    private List<String> categories;
    private Integer numberOfQuestions;

    private Map<String, Object> generationConfig;
}
