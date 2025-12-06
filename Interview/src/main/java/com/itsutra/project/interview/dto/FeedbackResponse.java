package com.itsutra.project.interview.dto;

import com.itsutra.project.interview.enums.Recommendation;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedbackResponse {

    private Long id;
    private Long interviewId;
    private Long providedBy;
    private Long providedFor;
    private Integer technicalSkillsRating;
    private Integer communicationSkillsRating;
    private Integer problemSolvingRating;
    private Integer culturalFitRating;
    private Integer overallRating;
    private String strengths;
    private String areasForImprovement;
    private String comments;
    private Recommendation recommendation;
    private Boolean isFinalFeedback;
    private Boolean isSharedWithCandidate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
