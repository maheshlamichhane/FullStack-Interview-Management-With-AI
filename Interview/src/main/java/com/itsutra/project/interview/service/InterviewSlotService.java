package com.itsutra.project.interview.service;

import com.itsutra.project.exception.InterviewException;
import com.itsutra.project.interview.dao.InterviewSlotRepository;
import com.itsutra.project.interview.dto.InterviewSlotRequest;
import com.itsutra.project.interview.dto.InterviewSlotResponse;
import com.itsutra.project.interview.mapper.interview_slot.InterviewSlotMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewSlotService {

    private final InterviewSlotRepository slotRepository;
//    private final InterviewService interviewService;
    private final InterviewSlotMapper interviewSlotMapper;
    private final Long interviewerId = 567284l;



    public Mono<InterviewSlotResponse> createSlot(InterviewSlotRequest request, Long interviewerId) {

        return checkSlotConflict(interviewerId, request.getStartTime(), request.getEndTime())
                .then(Mono.fromSupplier(() ->
                        interviewSlotMapper.toInterviewSlotEntity(request, interviewerId)
                ))
                .flatMap(slotRepository::save)
                .doOnSuccess(slot ->
                        log.info("Created interview slot for interviewer {}", interviewerId)
                )
                .map(interviewSlotMapper::toInterviewSlotResponse);
    }


    private Mono<Void> checkSlotConflict(Long interviewerId, LocalDateTime start, LocalDateTime end) {
        return slotRepository
                .findConflictingSlots(interviewerId, start, end)
                .hasElements()
                .filter(Boolean::booleanValue)
                .flatMap(conflict ->
                        Mono.error(new InterviewException("Slot already exists for provided time slots"))
                )
                .then();
    }


//    @Transactional
//    public InterviewSlotResponse createSlot(InterviewSlotRequest request) {
//        checkSlotConflict(interviewerId, request.getStartTime(), request.getEndTime());
//
//        InterviewSlot slot = interviewMapper.toInterviewSlotEntity(request,interviewerId);
//        InterviewSlot savedSlot = slotRepository.save(slot);
//
//        log.info("Created interview slot for interviewer: {}", interviewerId);
//        return interviewMapper.toInterviewSlotResponse(savedSlot);
//    }

//    @Transactional
//    public List<InterviewSlotResponse> getAvailableSlots() {
//        List<InterviewSlot> slots = slotRepository.findAvailableSlotsByInterviewer(interviewerId, LocalDateTime.now());
//        return slots.stream()
//                .map(interviewMapper::toInterviewSlotResponse)
//                .collect(Collectors.toList());
//    }
//
//
//    @Transactional
//    public List<InterviewSlotResponse> getSlotsByInterviewId(Long interviewId) {
//        Optional<InterviewSlot> slot = slotRepository.findByInterviewIdAndInterviewerId(interviewId,interviewerId);
//        if (slot.isEmpty()) {
//            throw new ResourceNotFoundException("No slots found for interview id: " + interviewId);
//        }
//        return slot.stream()
//                .map(interviewMapper::toInterviewSlotResponse)
//                .collect(Collectors.toList());
//    }
//
//
//    @Transactional
//    public InterviewSlotResponse cancelSlot(Long slotId, Long cancelledBy, String reason) throws Exception {
//        InterviewSlot slot = slotRepository.findByInterviewIdAndInterviewerId(slotId,interviewerId)
//                .orElseThrow(() -> new ResourceNotFoundException("Interview slot not found with id: " + slotId));
//
//        if(slot.getStatus() == SlotStatus.COMPLETED) {
//            throw new Exception("Already completed slot can't be cancelled");
//        }
//
//        slot.setStatus(SlotStatus.CANCELLED);
//        slot.setCancelledBy(cancelledBy);
//        slot.setCancellationReason(reason);
//        slot.setUpdatedAt(LocalDateTime.now());
//
//        InterviewSlot updatedSlot = slotRepository.save(slot);
//        log.info("Cancelled slot {} by user {}", slotId, cancelledBy);
//
//        return interviewMapper.toInterviewSlotResponse(updatedSlot);
//    }
//
//    @Transactional
//    public Optional<InterviewSlot> findInterviewSlotByIdAndInterviewerId(Long slogId, Long interviewerId) {
//        return slotRepository.findByInterviewIdAndInterviewerId(slogId,interviewerId);
//    }
//
//
////
////    @Transactional
////    public List<InterviewSlot> findInterviewSlotByInterviewId(Long id){
////        return slotRepository.findByInterviewId(id);
////    }
////
////    @Transactional
////    public void  saveAllInterviewSlot(List<InterviewSlot> slots){
////        slotRepository.saveAll(slots);
////    }
////
////    @Transactional
////    public void saveInterviewSlot(InterviewSlot slot){
////        slotRepository.save(slot);
////    }
////
    // Helper methods


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
