package com.core.project.interview.dto;

import com.core.project.interview.enums.ParticipantRole;
import com.core.project.interview.enums.ParticipantType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ParticipantRequest {


    @NotNull
    private Long participantId;

    @NotNull
    private ParticipantType participantType;

    @NotNull
    private ParticipantRole role;

    private Boolean isRequired;
}
