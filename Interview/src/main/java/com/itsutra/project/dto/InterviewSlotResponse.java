package com.itsutra.project.dto;

import com.itsutra.project.enums.SlotStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InterviewSlotResponse {
    private Long id;
    private Long interviewId;
    private Long interviewerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private SlotStatus status;
    private Long scheduledBy;
    private Long cancelledBy;
    private String cancellationReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
