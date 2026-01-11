package com.interview.project.interview.entity;

import com.interview.project.interview.enums.SlotStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(name = "interview_slots")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewSlot {

    @Id
    private Long id;


    @NotNull
    private Long interviewerId;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    @NotNull
    private SlotStatus status;

    private Long scheduledBy;

    private Long cancelledBy;
    private String cancellationReason;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
