package com.itsutra.project.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateInterviewRequest {

    @NotBlank(message = "Interview title is required")
    private String title;

    private String description;

    @NotNull(message = "Scheduled time is required")
    @Future(message = "Scheduled time must be in the future")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime scheduledTime;

    private Integer durationMinutes = 60;

    private String meetingUrl;

    @NotNull(message = "At least one participant is required")
    private List<ParticipantRequest> participants;
}
