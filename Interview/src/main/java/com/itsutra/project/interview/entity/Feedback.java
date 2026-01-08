package com.itsutra.project.interview.entity;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;


@Table(name = "feedbacks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback {


    @Id
    private Long id;

    @NotNull
    private Long providedBy; // interviewer ID

    @NotNull
    private Long providedFor; // candidate ID

    @Min(1)
    @Max(5)
    private Integer technicalSkillsRating;

    @Min(1)
    @Max(5)
    private Integer communicationSkillsRating;

    @Min(1)
    @Max(5)
    private Integer problemSolvingRating;

    @Min(1)
    @Max(5)
    private Integer culturalFitRating;

    @Min(1)
    @Max(5)
    private Integer overallRating;

    private String strengths;

    private String areasForImprovement;

    private String comments;


    private Boolean isFinalFeedback = false;

    private Boolean isSharedWithCandidate = false;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
