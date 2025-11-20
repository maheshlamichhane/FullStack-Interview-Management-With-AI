package com.itsutra.project.dto;

import com.itsutra.project.enums.Recommendation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponse {
    private Long id;
    private Long participantId;
    private String participantName;
    private String feedbackText;
    private Integer rating;
    private String strengths;
    private String areasForImprovement;
    private Recommendation recommendation;
    private LocalDateTime submittedAt;
}