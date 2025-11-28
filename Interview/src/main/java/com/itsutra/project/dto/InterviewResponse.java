package com.itsutra.project.dto;

import com.itsutra.project.enums.InterviewStatus;
import com.itsutra.project.enums.InterviewType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class InterviewResponse {
    private Long id;
    private Long candidateId;
    private Long interviewerId;
    private Long jobPositionId;
    private InterviewType interviewType;
    private InterviewStatus status;
    private String title;
    private String description;
    private LocalDateTime scheduledStartTime;
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
    private List<FeedbackResponse> feedbacks;
    private List<InterviewSlotResponse> slots;
    private List<ParticipantResponse> participants;
}
