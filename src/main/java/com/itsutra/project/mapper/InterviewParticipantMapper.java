package com.itsutra.project.mapper;

import com.itsutra.project.dto.ParticipantRequest;
import com.itsutra.project.entity.InterviewParticipant;
import com.itsutra.project.enums.ParticipantStatus;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;

@Component
public class InterviewParticipantMapper {

    public InterviewParticipant toEntity(ParticipantRequest request) {
        InterviewParticipant interviewParticipant = new InterviewParticipant();
        interviewParticipant.setUserId(request.getUserId());
        interviewParticipant.setUserType(request.getUserType());
        interviewParticipant.setParticipantRole(request.getParticipantRole());
        interviewParticipant.setStatus(ParticipantStatus.INVITED);
        interviewParticipant.setJoinedAt(LocalDateTime.now());
        return interviewParticipant;
    }
}
