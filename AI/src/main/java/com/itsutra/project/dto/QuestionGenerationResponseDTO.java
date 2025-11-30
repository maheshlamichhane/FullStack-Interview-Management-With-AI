package com.itsutra.project.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionGenerationResponseDTO {
    private String sessionId;
    private List<InterviewQuestionDTO> questions;
    private String difficultyLevel;
    private Integer totalQuestions;
    private Double generationTime;

    @Data
    public static class InterviewQuestionDTO {
        private String question;
        private String type; // TECHNICAL, BEHAVIORAL, SITUATIONAL
        private String category;
        private String difficulty;
        private List<String> keyPoints;
        private String sampleAnswer;
        private Integer expectedTime; // in minutes
    }
}
