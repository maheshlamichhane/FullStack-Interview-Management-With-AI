package com.interview.project.interview.service;

import com.interview.project.interview.dao.InterviewSlotRepository;
import com.interview.project.interview.dto.InterviewSlotRequest;
import com.interview.project.interview.dto.InterviewSlotResponse;
import com.interview.project.interview.enums.SlotStatus;
import com.interview.project.interview.exception.InterviewException;
import com.interview.project.interview.exception.SlotNotFoundException;
import com.interview.project.interview.mapper.interview_slot.InterviewSlotMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
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



    @Transactional
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


    @Transactional
    public Flux<InterviewSlotResponse> getAllSlots(){
        return slotRepository.findAll()
                .map(interviewSlotMapper::toInterviewSlotResponse);
    }




    @Transactional
    public Flux<InterviewSlotResponse> getAvailableSlots() {
        return slotRepository.findAvailableSlotsByInterviewer(interviewerId)
                .map(interviewSlotMapper::toInterviewSlotResponse);
    }

    @Transactional(readOnly = true)
    public Flux<InterviewSlotResponse> getSlotsByInterviewId(Integer interviewId, Long interviewerId) {

        return slotRepository.findByInterviewIdAndInterviewerId(interviewId, interviewerId)
                .switchIfEmpty(
                        Flux.error(new SlotNotFoundException(interviewId))
                )
                .map(interviewSlotMapper::toInterviewSlotResponse);
    }



    @Transactional
    public Mono<InterviewSlotResponse> cancelSlot(Integer slotId, Long interviewerId, Long cancelledBy, String reason) {

        return slotRepository.findSlotBySlotIdAndInterviewerId(interviewerId,slotId)
                .switchIfEmpty(Mono.error(new SlotNotFoundException(slotId)))
                .flatMap(slot -> {
                    if (slot.getStatus() == SlotStatus.COMPLETED) {
                        return Mono.error(new InterviewException("Slot is already completed"));
                    }

                    // Update slot
                    slot.setStatus(SlotStatus.CANCELLED);
                    slot.setCancelledBy(cancelledBy);
                    slot.setCancellationReason(reason);
                    slot.setUpdatedAt(LocalDateTime.now());

                    // Save updated slot
                    return slotRepository.save(slot);
                })
                // Map saved entity to DTO
                .map(interviewSlotMapper::toInterviewSlotResponse)
                // Logging side-effect
                .doOnNext(slotResponse ->
                        log.info("Cancelled slot {} by user {}", slotId, cancelledBy)
                );
    }

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
