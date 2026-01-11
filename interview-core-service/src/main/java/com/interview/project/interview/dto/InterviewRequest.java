package com.interview.project.interview.dto;

import com.interview.project.interview.enums.InterviewType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class InterviewRequest {
    @NotNull
    private Long candidateId;

    private Long jobPositionId;

    private Long slotId;

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

    private List<ParticipantRequest> participants = new ArrayList<>();
}
