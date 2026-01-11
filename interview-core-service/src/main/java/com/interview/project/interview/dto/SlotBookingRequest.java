package com.interview.project.interview.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SlotBookingRequest {


    @NotNull
    private Long candidateId;

    @NotNull
    private Long slotId;

    private String notes;
}
