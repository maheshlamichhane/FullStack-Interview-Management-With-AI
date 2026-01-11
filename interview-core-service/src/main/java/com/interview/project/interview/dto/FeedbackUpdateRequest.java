package com.interview.project.interview.dto;

import com.interview.project.interview.enums.Recommendation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FeedbackUpdateRequest {

    @NotNull(message = "This field is required")
    private Long id;

    @Min(1) @Max(5)
    private Integer technicalSkillsRating;

    @Min(1) @Max(5)
    private Integer communicationSkillsRating;

    @Min(1) @Max(5)
    private Integer problemSolvingRating;

    @Min(1) @Max(5)
    private Integer culturalFitRating;

    @Min(1) @Max(5)
    private Integer overallRating;

    private String strengths;
    private String areasForImprovement;
    private String comments;
    private Recommendation recommendation;
    private Boolean isFinalFeedback;
    private Boolean isSharedWithCandidate;
}
