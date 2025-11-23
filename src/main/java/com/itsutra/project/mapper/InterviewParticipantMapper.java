package com.itsutra.project.mapper;

import com.itsutra.project.dto.ParticipantRequest;
import com.itsutra.project.dto.ParticipantResponse;
import com.itsutra.project.entity.InterviewParticipant;
import com.itsutra.project.enums.ParticipantRole;
import com.itsutra.project.enums.ParticipantStatus;
import com.itsutra.project.enums.UserType;
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

    public InterviewParticipant toEntity(ParticipantResponse request) {
        InterviewParticipant interviewParticipant = new InterviewParticipant();
        interviewParticipant.setId(request.getId());
        interviewParticipant.setUserId(request.getUserId());
        interviewParticipant.setUserType(request.getUserType());
        interviewParticipant.setParticipantRole(request.getParticipantRole());
        interviewParticipant.setStatus(ParticipantStatus.INVITED);
        return interviewParticipant;

    }
}
