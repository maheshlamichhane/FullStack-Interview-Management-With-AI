package com.itsutra.project.dto;

import com.itsutra.project.enums.InterviewType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class InterviewRequest {
    @NotNull
    private Long candidateId;

    @NotNull
    private Long interviewerId;

    private Long jobPositionId;

    @NotNull
    private InterviewType interviewType;

    private String title;
    private String description;

    @NotNull
    @Future
    private LocalDateTime scheduledStartTime;

    @NotNull
    @Future
    private LocalDateTime scheduledEndTime;

    private Integer durationMinutes;
    private String meetingLink;
    private String location;
    private String notes;

    private List<ParticipantRequest> participants;
}
