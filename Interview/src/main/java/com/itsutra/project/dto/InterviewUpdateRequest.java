package com.itsutra.project.dto;


import com.itsutra.project.enums.InterviewStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InterviewUpdateRequest {
    private InterviewStatus status;
    private String title;
    private String description;
    private LocalDateTime scheduledStartTime;
    private LocalDateTime scheduledEndTime;
    private String meetingLink;
    private String location;
    private String notes;
    private LocalDateTime actualStartTime;
    private LocalDateTime actualEndTime;
}