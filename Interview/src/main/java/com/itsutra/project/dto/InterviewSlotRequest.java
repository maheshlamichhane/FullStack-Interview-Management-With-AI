package com.itsutra.project.dto;

import com.itsutra.project.entity.InterviewSlot;
import com.itsutra.project.enums.SlotStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InterviewSlotRequest {

    @NotNull
    @Future
    private LocalDateTime startTime;

    @NotNull
    @Future
    private LocalDateTime endTime;

    private SlotStatus status;
}
