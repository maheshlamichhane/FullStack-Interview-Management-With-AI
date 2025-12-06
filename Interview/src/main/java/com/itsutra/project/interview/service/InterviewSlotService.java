package com.itsutra.project.interview.service;

import com.itsutra.project.interview.dao.InterviewSlotRepository;
import com.itsutra.project.interview.dto.InterviewSlotRequest;
import com.itsutra.project.interview.dto.InterviewSlotResponse;
import com.itsutra.project.interview.entity.InterviewSlot;
import com.itsutra.project.interview.enums.SlotStatus;
import com.itsutra.project.interview.exception.ResourceNotFoundException;
import com.itsutra.project.interview.mapper.InterviewMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewSlotService {

    private final InterviewSlotRepository slotRepository;
//    private final InterviewService interviewService;
    private final InterviewMapper interviewMapper;
    private final Long interviewerId = 567284l;

    @Transactional
    public InterviewSlotResponse createSlot(InterviewSlotRequest request) {
        checkSlotConflict(interviewerId, request.getStartTime(), request.getEndTime());

        InterviewSlot slot = interviewMapper.toInterviewSlotEntity(request,interviewerId);
        InterviewSlot savedSlot = slotRepository.save(slot);

        log.info("Created interview slot for interviewer: {}", interviewerId);
        return interviewMapper.toInterviewSlotResponse(savedSlot);
    }

    @Transactional
    public List<InterviewSlotResponse> getAvailableSlots() {
        List<InterviewSlot> slots = slotRepository.findAvailableSlotsByInterviewer(interviewerId, LocalDateTime.now());
        return slots.stream()
                .map(interviewMapper::toInterviewSlotResponse)
                .collect(Collectors.toList());
    }


    @Transactional
    public List<InterviewSlotResponse> getSlotsByInterviewId(Long interviewId) {
        Optional<InterviewSlot> slot = slotRepository.findByInterviewIdAndInterviewerId(interviewId,interviewerId);
        if (slot.isEmpty()) {
            throw new ResourceNotFoundException("No slots found for interview id: " + interviewId);
        }
        return slot.stream()
                .map(interviewMapper::toInterviewSlotResponse)
                .collect(Collectors.toList());
    }


    @Transactional
    public InterviewSlotResponse cancelSlot(Long slotId, Long cancelledBy, String reason) throws Exception {
        InterviewSlot slot = slotRepository.findByInterviewIdAndInterviewerId(slotId,interviewerId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview slot not found with id: " + slotId));

        if(slot.getStatus() == SlotStatus.COMPLETED) {
            throw new Exception("Already completed slot can't be cancelled");
        }

        slot.setStatus(SlotStatus.CANCELLED);
        slot.setCancelledBy(cancelledBy);
        slot.setCancellationReason(reason);
        slot.setUpdatedAt(LocalDateTime.now());

        InterviewSlot updatedSlot = slotRepository.save(slot);
        log.info("Cancelled slot {} by user {}", slotId, cancelledBy);

        return interviewMapper.toInterviewSlotResponse(updatedSlot);
    }

    @Transactional
    public Optional<InterviewSlot> findInterviewSlotByIdAndInterviewerId(Long slogId, Long interviewerId) {
        return slotRepository.findByInterviewIdAndInterviewerId(slogId,interviewerId);
    }


//
//    @Transactional
//    public List<InterviewSlot> findInterviewSlotByInterviewId(Long id){
//        return slotRepository.findByInterviewId(id);
//    }
//
//    @Transactional
//    public void  saveAllInterviewSlot(List<InterviewSlot> slots){
//        slotRepository.saveAll(slots);
//    }
//
//    @Transactional
//    public void saveInterviewSlot(InterviewSlot slot){
//        slotRepository.save(slot);
//    }
//
    // Helper methods
    private void checkSlotConflict(Long interviewerId, LocalDateTime startTime, LocalDateTime endTime) {
        List<InterviewSlot> conflictingSlots = slotRepository.findSlotsByInterviewerAndTimeRange(interviewerId, startTime, endTime);

        if (!conflictingSlots.isEmpty()) {
            throw new IllegalStateException("Interviewer has conflicting slots during the requested time");
        }
    }
//
//    private Interview createDraftInterview(Long candidateId, Long interviewerId) {
//        Interview interview = Interview.builder()
//                .candidateId(candidateId)
//                .interviewerId(interviewerId)
//                .interviewType(InterviewType.TECHNICAL)
//                .status(InterviewStatus.DRAFT)
//                .title("Draft Interview")
//                .scheduledStartTime(LocalDateTime.now().plusDays(1))
//                .scheduledEndTime(LocalDateTime.now().plusDays(1).plusHours(1))
//                .build();
//
//        return interviewService.saveInterview(interview);
//    }
//
//






}
