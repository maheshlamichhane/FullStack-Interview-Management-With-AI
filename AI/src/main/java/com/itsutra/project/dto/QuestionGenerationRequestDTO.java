package com.itsutra.project.dto;

import com.itsutra.project.enums.QuestionType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class QuestionGenerationRequestDTO {

    @NotBlank
    private String jobRole;

    private String experienceLevel;
    private List<String> technicalSkills;
    private List<String> softSkills;
    private Integer numberOfQuestions = 10;
    private String difficultyLevel = "MEDIUM";
    private QuestionType questionType = QuestionType.TECHNICAL;
}
