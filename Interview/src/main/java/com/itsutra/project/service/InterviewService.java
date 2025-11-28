package com.itsutra.project.service;


import com.itsutra.project.dao.FeedbackRepository;
import com.itsutra.project.dao.InterviewParticipantRepository;
import com.itsutra.project.dao.InterviewRepository;
import com.itsutra.project.dao.InterviewSlotRepository;
import com.itsutra.project.dto.InterviewRequest;
import com.itsutra.project.dto.InterviewResponse;
import com.itsutra.project.dto.InterviewUpdateRequest;
import com.itsutra.project.dto.ParticipantRequest;
import com.itsutra.project.entity.Interview;
import com.itsutra.project.entity.InterviewParticipant;
import com.itsutra.project.entity.InterviewSlot;
import com.itsutra.project.enums.InterviewStatus;
import com.itsutra.project.enums.ParticipantRole;
import com.itsutra.project.enums.ParticipantType;
import com.itsutra.project.enums.SlotStatus;
import com.itsutra.project.exception.ResourceNotFoundException;
import com.itsutra.project.mapper.InterviewMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final InterviewSlotRepository slotRepository;
    private final FeedbackRepository feedbackRepository;
    private final InterviewParticipantRepository participantRepository;
    private final InterviewMapper interviewMapper;

    @Transactional
    public InterviewResponse createInterview(InterviewRequest request) {

        log.info("Creating new interview for candidate: {}", request.getCandidateId());

        // Create interview
        Interview interview = interviewMapper.toInterviewEntity(request);
        interview.setStatus(InterviewStatus.SCHEDULED);

        Interview savedInterview = interviewRepository.save(interview);

        // Create default slot
        createDefaultInterviewSlot(savedInterview, request);

        // Create participants
        createInterviewParticipants(savedInterview, request.getParticipants());

        return interviewMapper.toInterviewResponse(savedInterview);
    }

    public InterviewResponse getInterviewById(Long id) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found with id: " + id));
        return interviewMapper.toInterviewResponse(interview);
    }

    public List<InterviewResponse> getInterviewsByCandidate(Long candidateId) {
        List<Interview> interviews = interviewRepository.findByCandidateId(candidateId);
        return interviews.stream()
                .map(interviewMapper::toInterviewResponse)
                .collect(Collectors.toList());
    }

    public List<InterviewResponse> getInterviewsByInterviewer(Long interviewerId) {
        List<Interview> interviews = interviewRepository.findByInterviewerId(interviewerId);
        return interviews.stream()
                .map(interviewMapper::toInterviewResponse)
                .collect(Collectors.toList());
    }

    public Page<InterviewResponse> getInterviewsByStatus(InterviewStatus status, Pageable pageable) {
        Page<Interview> interviews = interviewRepository.findByStatus(status, pageable);
        return interviews.map(interviewMapper::toInterviewResponse);
    }

    @Transactional
    public InterviewResponse updateInterview(Long id, InterviewUpdateRequest request) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found with id: " + id));

        if (request.getStatus() != null) {
            interview.setStatus(request.getStatus());
        }
        if (request.getTitle() != null) {
            interview.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            interview.setDescription(request.getDescription());
        }
        if (request.getScheduledStartTime() != null) {
            interview.setScheduledStartTime(request.getScheduledStartTime());
        }
        if (request.getScheduledEndTime() != null) {
            interview.setScheduledEndTime(request.getScheduledEndTime());
        }
        if (request.getMeetingLink() != null) {
            interview.setMeetingLink(request.getMeetingLink());
        }
        if (request.getLocation() != null) {
            interview.setLocation(request.getLocation());
        }
        if (request.getNotes() != null) {
            interview.setNotes(request.getNotes());
        }
        if (request.getActualStartTime() != null) {
            interview.setActualStartTime(request.getActualStartTime());
        }
        if (request.getActualEndTime() != null) {
            interview.setActualEndTime(request.getActualEndTime());
        }

        Interview updatedInterview = interviewRepository.save(interview);
        return interviewMapper.toInterviewResponse(updatedInterview);
    }

    @Transactional
    public void deleteInterview(Long id) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found with id: " + id));
        interviewRepository.delete(interview);
        log.info("Deleted interview with id: {}", id);
    }

    @Transactional
    public InterviewResponse startInterview(Long id) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found with id: " + id));

        if (interview.getStatus() != InterviewStatus.SCHEDULED) {
            throw new IllegalStateException("Interview must be in SCHEDULED status to start");
        }

        interview.setStatus(InterviewStatus.IN_PROGRESS);
        interview.setActualStartTime(LocalDateTime.now());

        Interview updatedInterview = interviewRepository.save(interview);
        log.info("Started interview with id: {}", id);

        return interviewMapper.toInterviewResponse(updatedInterview);
    }

    @Transactional
    public InterviewResponse completeInterview(Long id) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found with id: " + id));

        if (interview.getStatus() != InterviewStatus.IN_PROGRESS) {
            throw new IllegalStateException("Interview must be in IN_PROGRESS status to complete");
        }

        interview.setStatus(InterviewStatus.COMPLETED);
        interview.setActualEndTime(LocalDateTime.now());

        // Calculate duration if not set
        if (interview.getActualStartTime() != null && interview.getActualEndTime() != null) {
            long durationMinutes = java.time.Duration.between(
                    interview.getActualStartTime(), interview.getActualEndTime()
            ).toMinutes();
            interview.setDurationMinutes((int) durationMinutes);
        }

        Interview updatedInterview = interviewRepository.save(interview);
        log.info("Completed interview with id: {}", id);

        return interviewMapper.toInterviewResponse(updatedInterview);
    }

    @Transactional
    public InterviewResponse cancelInterview(Long id, String cancellationReason) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found with id: " + id));

        interview.setStatus(InterviewStatus.CANCELLED);
        interview.setNotes(interview.getNotes() + "\nCancellation Reason: " + cancellationReason);

        // Cancel associated slots
        List<InterviewSlot> slots = slotRepository.findByInterviewId(id);
        slots.forEach(slot -> {
            slot.setStatus(SlotStatus.CANCELLED);
            slot.setCancellationReason(cancellationReason);
        });
        slotRepository.saveAll(slots);

        Interview updatedInterview = interviewRepository.save(interview);
        log.info("Cancelled interview with id: {}", id);

        return interviewMapper.toInterviewResponse(updatedInterview);
    }

    // Helper methods
    private void createDefaultInterviewSlot(Interview interview, InterviewRequest request) {
        InterviewSlot slot = InterviewSlot.builder()
                .interview(interview)
                .interviewerId(request.getInterviewerId())
                .startTime(request.getScheduledStartTime())
                .endTime(request.getScheduledEndTime())
                .status(SlotStatus.BOOKED)
                .scheduledBy(request.getInterviewerId())
                .build();
        slotRepository.save(slot);
    }

    private void createInterviewParticipants(Interview interview, List<ParticipantRequest> participantRequests) {
        if (participantRequests == null || participantRequests.isEmpty()) {
            // Create default participants
            createDefaultParticipants(interview);
            return;
        }

        List<InterviewParticipant> participants = participantRequests.stream()
                .map(request -> interviewMapper.toParticipantEntity(request, interview))
                .collect(Collectors.toList());

        participantRepository.saveAll(participants);
    }

    private void createDefaultParticipants(Interview interview) {

        // Add candidate
        InterviewParticipant candidate = InterviewParticipant.builder()
                .interview(interview)
                .participantId(interview.getCandidateId())
                .participantType(ParticipantType.INTERNAL_USER)
                .role(ParticipantRole.CANDIDATE)
                .isRequired(true)
                .confirmedAttendance(false)
                .build();

        // Add interviewer
        InterviewParticipant interviewer = InterviewParticipant.builder()
                .interview(interview)
                .participantId(interview.getInterviewerId())
                .participantType(ParticipantType.INTERNAL_USER)
                .role(ParticipantRole.INTERVIEWER)
                .isRequired(true)
                .confirmedAttendance(false)
                .build();

        participantRepository.save(candidate);
        participantRepository.save(interviewer);
    }

    public Long getInterviewCountByCandidate(Long candidateId) {
        return interviewRepository.countCompletedInterviewsByCandidate(candidateId);
    }
}
