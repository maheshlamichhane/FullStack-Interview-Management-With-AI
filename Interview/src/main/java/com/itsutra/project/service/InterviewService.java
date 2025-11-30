package com.itsutra.project.service;


import com.itsutra.project.dao.InterviewRepository;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final InterviewSlotService interviewSlotService;
//    private final InterviewParticipantService interviewParticipantService;
    private final InterviewMapper interviewMapper;
    private final Long interviewerId = 567284l;


    @Transactional
    public Interview saveInterview(Interview interview){
        return interviewRepository.save(interview);
    }


    @Transactional
    public Optional<Interview> findInterviewByIdAndParticipantId(Long id,Long participantId) throws ResourceNotFoundException {
        return interviewRepository.findByIdAndParticipantsParticipantId(id,participantId);
    }


//
//    @Transactional
//    public List<Interview> findInterviewByCandidateIdAndStatus(Long candidateId, InterviewStatus status) throws ResourceNotFoundException {
//        return interviewRepository.findByCandidateIdAndStatus(candidateId,status);
//    }
//
//
//
    @Transactional
    public InterviewResponse createInterview(InterviewRequest request) throws Exception {

        log.info("Creating new interview for candidate: {}", request.getCandidateId());

        // Check if there is already a interview associated with this id
        InterviewSlot interviewSlot = interviewSlotService.findInterviewSlotByIdAndInterviewerId(request.getSlotId(),interviewerId)
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found with id: " + request.getSlotId()));

        if(interviewSlot.getInterview() != null){
            throw new Exception("Slot is already occupied");
        }

        if(interviewSlot.getStatus() != SlotStatus.AVAILABLE){
            throw new Exception("Slot is not available");
        }

        // Create interview
        Interview interview = interviewMapper.toInterviewEntity(request,interviewerId);
        interview.setStatus(InterviewStatus.SCHEDULED);

        interviewSlot.setInterview(interview);
        interviewSlot.setScheduledBy(interviewerId);
        interviewSlot.setStatus(SlotStatus.BOOKED);
        interview.setSlots(List.of(interviewSlot));

        Interview savedInterview = interviewRepository.save(interview);
        return interviewMapper.toInterviewResponse(savedInterview);
    }

    @Transactional
    public InterviewResponse getInterviewById(Long id) {
        Interview interview = interviewRepository.findByIdAndInterviewerId(id,interviewerId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found with id: " + id));
        return interviewMapper.toInterviewResponse(interview);
    }

    @Transactional
    public List<InterviewResponse> getInterviewsByCandidate(Long candidateId) {
        List<Interview> interviews = interviewRepository.findByCandidateIdAndInterviewerId(candidateId,interviewerId);
        return interviews.stream()
                .map(interviewMapper::toInterviewResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<InterviewResponse> getInterviewsByInterviewer(Long interviewerId) {
        List<Interview> interviews = interviewRepository.findByInterviewerId(interviewerId);
        return interviews.stream()
                .map(interviewMapper::toInterviewResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<InterviewResponse> getInterviewsByStatus(InterviewStatus status) {
        List<Interview> interviews = interviewRepository.findByStatusAndInterviewerId(status, interviewerId);
        return interviews.stream().map(interviewMapper::toInterviewResponse).collect(Collectors.toList());
    }


    @Transactional
    public InterviewResponse updateInterview(InterviewUpdateRequest request) {
        Interview interview = interviewRepository.findByIdAndInterviewerId(request.getId(),interviewerId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found with id: " + request.getId()));

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
        Interview interview = interviewRepository.findByIdAndInterviewerId(id,interviewerId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found with id: " + id));
        interviewRepository.delete(interview);
        log.info("Deleted interview with id: {}", id);
    }


    @Transactional
    public InterviewResponse startInterview(Long id) {
        Interview interview = interviewRepository.findByIdAndInterviewerId(id,interviewerId)
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
        Interview interview = interviewRepository.findByIdAndInterviewerId(id,interviewerId)
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

        Interview interview = interviewRepository.findByIdAndInterviewerId(id,interviewerId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found with id: " + id));

        if(interview.getStatus() == InterviewStatus.IN_PROGRESS){
            throw new IllegalStateException("Interview is already started so can't cancel interview");
        }
        if (interview.getStatus() == InterviewStatus.COMPLETED) {
            throw new IllegalStateException("Interview is already completed");
        }
        if(interview.getStatus() == InterviewStatus.CANCELLED){
            throw new IllegalStateException("Interview is already cancelled");
        }

        interview.setStatus(InterviewStatus.CANCELLED);
        interview.setNotes(interview.getNotes() + "\nCancellation Reason: " + cancellationReason);

        // Cancel associated slots
        List<InterviewSlot> slots = interview.getSlots();
        InterviewSlot slot = slots.get(0);

        if(slot.getStatus() == SlotStatus.COMPLETED) {
            throw new IllegalStateException("Interview is already completed");
        }

        slot.setStatus(SlotStatus.CANCELLED);
        slot.setCancellationReason(cancellationReason);

        interview.setSlots(new ArrayList<>(Arrays.asList(slot)));

        Interview updatedInterview = interviewRepository.save(interview);
        log.info("Cancelled interview with id: {}", id);

        return interviewMapper.toInterviewResponse(updatedInterview);
    }

    @Transactional
    public Long getInterviewCountByCandidate(Long candidateId) {
        return interviewRepository.countByInterviewerIdAndCandidateIdAndStatus(interviewerId, candidateId, InterviewStatus.COMPLETED);
    }



//
//    // Helper methods
//    private void createDefaultInterviewSlot(Interview interview, InterviewRequest request) {
//        InterviewSlot slot = InterviewSlot.builder()
//                .interview(interview)
//                .interviewerId(request.getInterviewerId())
//                .startTime(request.getScheduledStartTime())
//                .endTime(request.getScheduledEndTime())
//                .status(SlotStatus.BOOKED)
//                .scheduledBy(request.getInterviewerId())
//                .build();
//        interviewSlotService.saveInterviewSlot(slot);
//    }
//
//    private void createInterviewParticipants(Interview interview, List<ParticipantRequest> participantRequests) {
//        if (participantRequests == null || participantRequests.isEmpty()) {
//            // Create default participants
//            createDefaultParticipants(interview);
//            return;
//        }
//
//        List<InterviewParticipant> participants = participantRequests.stream()
//                .map(request -> interviewMapper.toParticipantEntity(request, interview))
//                .collect(Collectors.toList());
//
//        interviewParticipantService.saveAllParticipants(participants);
//    }
//
//    private void createDefaultParticipants(Interview interview) {
//
//        // Add candidate
//        InterviewParticipant candidate = InterviewParticipant.builder()
//                .interview(interview)
//                .participantId(interview.getCandidateId())
//                .participantType(ParticipantType.INTERNAL_USER)
//                .role(ParticipantRole.CANDIDATE)
//                .isRequired(true)
//                .confirmedAttendance(false)
//                .build();
//
//        // Add interviewer
//        InterviewParticipant interviewer = InterviewParticipant.builder()
//                .interview(interview)
//                .participantId(interview.getInterviewerId())
//                .participantType(ParticipantType.INTERNAL_USER)
//                .role(ParticipantRole.INTERVIEWER)
//                .isRequired(true)
//                .confirmedAttendance(false)
//                .build();
//
//        interviewParticipantService.saveParticipants(candidate);
//        interviewParticipantService.saveParticipants(interviewer);
//    }

}
