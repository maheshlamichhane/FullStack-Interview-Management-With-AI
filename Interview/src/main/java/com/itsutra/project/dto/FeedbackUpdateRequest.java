package com.itsutra.project.dto;

import com.itsutra.project.enums.Recommendation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class FeedbackUpdateRequest {


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
