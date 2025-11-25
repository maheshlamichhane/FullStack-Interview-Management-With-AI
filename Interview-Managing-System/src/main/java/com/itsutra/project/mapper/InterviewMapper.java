package com.itsutra.project.mapper;

import com.itsutra.project.dto.*;
import com.itsutra.project.entity.Interview;
import com.itsutra.project.entity.InterviewParticipant;
import com.itsutra.project.enums.InterviewStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InterviewMapper {

    public Interview toEntity(CreateInterviewRequest request) {
        Interview interview = new Interview();
        interview.setTitle(request.getTitle());
        interview.setDescription(request.getDescription());
        interview.setStatus(InterviewStatus.DRAFT);
        interview.setScheduledTime(request.getScheduledTime());
        interview.setDurationMinutes(request.getDurationMinutes());
        interview.setMeetingUrl(request.getMeetingUrl());
        return interview;
    }


    public Interview toEntity(UpdateInterviewRequest request,Interview interview) {
        request.setId(request.getId());
        interview.setTitle(request.getTitle());
        interview.setDescription(request.getDescription());
        interview.setStatus(request.getStatus());
        interview.setScheduledTime(request.getScheduledTime());
        interview.setDurationMinutes(request.getDurationMinutes());
        interview.setMeetingUrl(request.getMeetingUrl());
        return interview;
    }


    public InterviewResponse toResponse(Interview interview) {
        InterviewResponse response = new InterviewResponse();
        response.setId(interview.getId());
        response.setTitle(interview.getTitle());
        response.setDescription(interview.getDescription());
        response.setStatus(interview.getStatus());
        response.setScheduledTime(interview.getScheduledTime());
        response.setDurationMinutes(interview.getDurationMinutes());
        response.setMeetingUrl(interview.getMeetingUrl());
        response.setCreatedBy(interview.getCreatedBy());
        response.setCreatedAt(interview.getCreatedAt());
        response.setUpdatedAt(interview.getUpdatedAt());
        List<ParticipantResponse> participants = new ArrayList<>();
        for(InterviewParticipant participant : interview.getParticipants()) {
           ParticipantResponse participantResponse = new ParticipantResponse();
           participantResponse.setId(participant.getId());
           participantResponse.setUserId(participant.getUserId());
           participantResponse.setUserType(participant.getUserType());
           participantResponse.setParticipantRole(participant.getParticipantRole());
           participantResponse.setStatus(participant.getStatus());
           participantResponse.setCreatedAt(participant.getCreatedAt());
           participantResponse.setJoinedAt(participant.getJoinedAt());
           participants.add(participantResponse);
        }
        response.setParticipants(participants);
        return response;
    }
}
