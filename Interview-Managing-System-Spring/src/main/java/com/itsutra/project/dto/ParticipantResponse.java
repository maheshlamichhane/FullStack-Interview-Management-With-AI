package com.itsutra.project.dto;

import com.itsutra.project.enums.ParticipantRole;
import com.itsutra.project.enums.ParticipantStatus;
import com.itsutra.project.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantResponse {
    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private UserType userType;
    private ParticipantRole participantRole;
    private ParticipantStatus status;
    private LocalDateTime joinedAt;
    private LocalDateTime respondedAt;
    private LocalDateTime createdAt;
}
