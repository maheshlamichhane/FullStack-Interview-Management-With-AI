package com.itsutra.project.service;

import com.itsutra.project.dao.InterviewRepository;
import com.itsutra.project.dao.InterviewSlotRepository;
import com.itsutra.project.dto.InterviewSlotRequest;
import com.itsutra.project.dto.InterviewSlotResponse;
import com.itsutra.project.dto.SlotBookingRequest;
import com.itsutra.project.entity.Interview;
import com.itsutra.project.entity.InterviewSlot;
import com.itsutra.project.enums.InterviewStatus;
import com.itsutra.project.enums.InterviewType;
import com.itsutra.project.enums.SlotStatus;
import com.itsutra.project.exception.ResourceNotFoundException;
import com.itsutra.project.mapper.InterviewMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewSlotService {

    private final InterviewSlotRepository slotRepository;
    private final InterviewRepository interviewRepository;
    private final InterviewMapper interviewMapper;

    @Transactional
    public InterviewSlotResponse createSlot(InterviewSlotRequest request, Long interviewId) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found with id: " + interviewId));

        // Check for slot conflicts
        checkSlotConflict(request.getInterviewerId(), request.getStartTime(), request.getEndTime());

        InterviewSlot slot = interviewMapper.toInterviewSlotEntity(request, interview);
        InterviewSlot savedSlot = slotRepository.save(slot);

        log.info("Created interview slot for interviewer: {}", request.getInterviewerId());
        return interviewMapper.toInterviewSlotResponse(savedSlot);
    }

    public List<InterviewSlotResponse> getAvailableSlotsByInterviewer(Long interviewerId) {
        List<InterviewSlot> slots = slotRepository.findAvailableSlotsByInterviewer(interviewerId, LocalDateTime.now());
        return slots.stream()
                .map(interviewMapper::toInterviewSlotResponse)
                .collect(Collectors.toList());
    }

    public List<InterviewSlotResponse> getSlotsByInterview(Long interviewId) {
        List<InterviewSlot> slots = slotRepository.findByInterviewId(interviewId);
        return slots.stream()
                .map(interviewMapper::toInterviewSlotResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public InterviewSlotResponse bookSlot(SlotBookingRequest request) {
        InterviewSlot slot = slotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new ResourceNotFoundException("Interview slot not found with id: " + request.getSlotId()));

        if (slot.getStatus() != SlotStatus.AVAILABLE) {
            throw new IllegalStateException("Slot is not available for booking");
        }

        // Get or create interview
        Interview interview = interviewRepository.findByCandidateIdAndStatus(request.getCandidateId(), InterviewStatus.DRAFT)
                .stream()
                .findFirst()
                .orElseGet(() -> createDraftInterview(request.getCandidateId(), slot.getInterviewerId()));

        slot.setInterview(interview);
        slot.setStatus(SlotStatus.BOOKED);
        slot.setScheduledBy(request.getCandidateId()); // Assuming candidate books it

        InterviewSlot updatedSlot = slotRepository.save(slot);
        log.info("Booked slot {} for candidate {}", request.getSlotId(), request.getCandidateId());

        return interviewMapper.toInterviewSlotResponse(updatedSlot);
    }

    @Transactional
    public InterviewSlotResponse cancelSlot(Long slotId, Long cancelledBy, String reason) {
        InterviewSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview slot not found with id: " + slotId));

        slot.setStatus(SlotStatus.CANCELLED);
        slot.setCancelledBy(cancelledBy);
        slot.setCancellationReason(reason);
        slot.setUpdatedAt(LocalDateTime.now());

        InterviewSlot updatedSlot = slotRepository.save(slot);
        log.info("Cancelled slot {} by user {}", slotId, cancelledBy);

        return interviewMapper.toInterviewSlotResponse(updatedSlot);
    }

    // Helper methods
    private void checkSlotConflict(Long interviewerId, LocalDateTime startTime, LocalDateTime endTime) {
        List<InterviewSlot> conflictingSlots = slotRepository.findSlotsByInterviewerAndTimeRange(interviewerId, startTime, endTime);

        if (!conflictingSlots.isEmpty()) {
            throw new IllegalStateException("Interviewer has conflicting slots during the requested time");
        }
    }

    private Interview createDraftInterview(Long candidateId, Long interviewerId) {
        Interview interview = Interview.builder()
                .candidateId(candidateId)
                .interviewerId(interviewerId)
                .interviewType(InterviewType.TECHNICAL)
                .status(InterviewStatus.DRAFT)
                .title("Draft Interview")
                .scheduledStartTime(LocalDateTime.now().plusDays(1))
                .scheduledEndTime(LocalDateTime.now().plusDays(1).plusHours(1))
                .build();

        return interviewRepository.save(interview);
    }
}
