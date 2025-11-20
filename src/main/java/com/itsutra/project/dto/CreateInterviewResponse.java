package com.itsutra.project.dto;

import com.itsutra.project.enums.InterviewStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateInterviewResponse {
    private Long id;
    private String title;
    private String description;
    private InterviewStatus status;
    private LocalDateTime scheduledTime;
    private Integer durationMinutes;
    private String meetingUrl;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ParticipantResponse> participants;
    private List<FeedbackResponse> feedBackResponses;


}
