package com.core.project.interview.entity;

import com.core.project.interview.enums.InterviewStatus;
import com.core.project.interview.enums.InterviewType;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;


@Table(name = "interviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interview {


    @Id
    private Long id;

    @NotNull
    private Long candidateId;

    @NotNull
    private Long interviewerId;

    private Long jobPositionId;

    @NotNull
    private InterviewType interviewType;

    @NotNull
    private InterviewStatus status;

    private String title;

    private String description;

    @NotNull
    private LocalDateTime scheduledStartTime;

    @NotNull
    private LocalDateTime scheduledEndTime;

    private LocalDateTime actualStartTime;

    private LocalDateTime actualEndTime;

    private Integer durationMinutes;

    private String meetingLink;

    private String location;

    private String notes;

    private Double overallRating;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}