package com.itsutra.project.dto;

import com.itsutra.project.enums.ParticipantRole;
import com.itsutra.project.enums.UserType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ParticipantRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "User type is required")
    private UserType userType;

    @NotNull(message = "Participant role is required")
    private ParticipantRole participantRole;
}
