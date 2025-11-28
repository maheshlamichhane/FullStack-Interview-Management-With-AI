package com.itsutra.project.mapper;


import com.itsutra.project.dto.*;
import com.itsutra.project.entity.Feedback;
import com.itsutra.project.entity.Interview;
import com.itsutra.project.entity.InterviewParticipant;
import com.itsutra.project.entity.InterviewSlot;
import com.itsutra.project.enums.InterviewStatus;
import com.itsutra.project.enums.SlotStatus;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class InterviewMapper {

    // Interview Mappings
    public Interview toInterviewEntity(InterviewRequest request) {
        return Interview.builder()
                .candidateId(request.getCandidateId())
                .interviewerId(request.getInterviewerId())
                .jobPositionId(request.getJobPositionId())
                .interviewType(request.getInterviewType())
                .status(InterviewStatus.DRAFT)
                .title(request.getTitle())
                .description(request.getDescription())
                .scheduledStartTime(request.getScheduledStartTime())
                .scheduledEndTime(request.getScheduledEndTime())
                .durationMinutes(request.getDurationMinutes())
                .meetingLink(request.getMeetingLink())
                .location(request.getLocation())
                .notes(request.getNotes())
                .build();
    }

    public InterviewResponse toInterviewResponse(Interview entity) {
        InterviewResponse response = new InterviewResponse();
        response.setId(entity.getId());
        response.setCandidateId(entity.getCandidateId());
        response.setInterviewerId(entity.getInterviewerId());
        response.setJobPositionId(entity.getJobPositionId());
        response.setInterviewType(entity.getInterviewType());
        response.setStatus(entity.getStatus());
        response.setTitle(entity.getTitle());
        response.setDescription(entity.getDescription());
        response.setScheduledStartTime(entity.getScheduledStartTime());
        response.setScheduledEndTime(entity.getScheduledEndTime());
        response.setActualStartTime(entity.getActualStartTime());
        response.setActualEndTime(entity.getActualEndTime());
        response.setDurationMinutes(entity.getDurationMinutes());
        response.setMeetingLink(entity.getMeetingLink());
        response.setLocation(entity.getLocation());
        response.setNotes(entity.getNotes());
        response.setOverallRating(entity.getOverallRating());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getFeedbacks() != null) {
            response.setFeedbacks(entity.getFeedbacks().stream()
                    .map(this::toFeedbackResponse)
                    .collect(Collectors.toList()));
        }

        if (entity.getSlots() != null) {
            response.setSlots(entity.getSlots().stream()
                    .map(this::toInterviewSlotResponse)
                    .collect(Collectors.toList()));
        }

        return response;
    }

    // InterviewSlot Mappings
    public InterviewSlot toInterviewSlotEntity(InterviewSlotRequest request, Interview interview) {
        return InterviewSlot.builder()
                .interview(interview)
                .interviewerId(request.getInterviewerId())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(request.getStatus() != null ? request.getStatus() : SlotStatus.AVAILABLE)
                .build();
    }

    public InterviewSlotResponse toInterviewSlotResponse(InterviewSlot entity) {
        InterviewSlotResponse response = new InterviewSlotResponse();
        response.setId(entity.getId());
        response.setInterviewId(entity.getInterview().getId());
        response.setInterviewerId(entity.getInterviewerId());
        response.setStartTime(entity.getStartTime());
        response.setEndTime(entity.getEndTime());
        response.setStatus(entity.getStatus());
        response.setScheduledBy(entity.getScheduledBy());
        response.setCancelledBy(entity.getCancelledBy());
        response.setCancellationReason(entity.getCancellationReason());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }

    // Feedback Mappings
    public Feedback toFeedbackEntity(FeedbackRequest request, Interview interview) {
        return Feedback.builder()
                .interview(interview)
                .providedBy(request.getProvidedBy())
                .providedFor(request.getProvidedFor())
                .technicalSkillsRating(request.getTechnicalSkillsRating())
                .communicationSkillsRating(request.getCommunicationSkillsRating())
                .problemSolvingRating(request.getProblemSolvingRating())
                .culturalFitRating(request.getCulturalFitRating())
                .overallRating(request.getOverallRating())
                .strengths(request.getStrengths())
                .areasForImprovement(request.getAreasForImprovement())
                .comments(request.getComments())
                .recommendation(request.getRecommendation())
                .isFinalFeedback(request.getIsFinalFeedback() != null ? request.getIsFinalFeedback() : false)
                .isSharedWithCandidate(request.getIsSharedWithCandidate() != null ? request.getIsSharedWithCandidate() : false)
                .build();
    }

    public FeedbackResponse toFeedbackResponse(Feedback entity) {
        FeedbackResponse response = new FeedbackResponse();
        response.setId(entity.getId());
        response.setInterviewId(entity.getInterview().getId());
        response.setProvidedBy(entity.getProvidedBy());
        response.setProvidedFor(entity.getProvidedFor());
        response.setTechnicalSkillsRating(entity.getTechnicalSkillsRating());
        response.setCommunicationSkillsRating(entity.getCommunicationSkillsRating());
        response.setProblemSolvingRating(entity.getProblemSolvingRating());
        response.setCulturalFitRating(entity.getCulturalFitRating());
        response.setOverallRating(entity.getOverallRating());
        response.setStrengths(entity.getStrengths());
        response.setAreasForImprovement(entity.getAreasForImprovement());
        response.setComments(entity.getComments());
        response.setRecommendation(entity.getRecommendation());
        response.setIsFinalFeedback(entity.getIsFinalFeedback());
        response.setIsSharedWithCandidate(entity.getIsSharedWithCandidate());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }

    // Participant Mappings
    public InterviewParticipant toParticipantEntity(ParticipantRequest request, Interview interview) {
        return InterviewParticipant.builder()
                .interview(interview)
                .participantId(request.getParticipantId())
                .participantType(request.getParticipantType())
                .role(request.getRole())
                .isRequired(request.getIsRequired() != null ? request.getIsRequired() : true)
                .confirmedAttendance(false)
                .build();
    }

    public ParticipantResponse toParticipantResponse(InterviewParticipant entity) {
        ParticipantResponse response = new ParticipantResponse();
        response.setId(entity.getId());
        response.setInterviewId(entity.getInterview().getId());
        response.setParticipantId(entity.getParticipantId());
        response.setParticipantType(entity.getParticipantType());
        response.setRole(entity.getRole());
        response.setIsRequired(entity.getIsRequired());
        response.setConfirmedAttendance(entity.getConfirmedAttendance());
        response.setAttended(entity.getAttended());
        response.setCreatedAt(entity.getCreatedAt());
        return response;
    }
}
