package com.itsutra.project.interview.controller;


import com.itsutra.project.interview.dto.AccountsMsgDto;
import com.itsutra.project.interview.dto.InterviewSlotRequest;
import com.itsutra.project.interview.dto.InterviewSlotResponse;

import com.itsutra.project.interview.service.InterviewSlotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/interviews/interview-slots")
@RequiredArgsConstructor
@Slf4j
public class InterviewSlotController {

    private final InterviewSlotService slotService;
    private final Long interviewerId = 567284l;
    private final StreamBridge streamBridge;


    @PostMapping
    public Mono<ResponseEntity<InterviewSlotResponse>> createSlot(@Valid @RequestBody InterviewSlotRequest request) {
        return slotService.createSlot(request, interviewerId)
                .doOnSuccess(response -> sendCommunication())
                .map(response ->
                        ResponseEntity.status(HttpStatus.CREATED).body(response)
                );
    }


    private void sendCommunication() {
        var accountsMsgDto = new AccountsMsgDto(343434l,"mahesh","mahesh@gmail.com","9818567284");
        log.info("Sending Communicaiotn request for the details: {}",accountsMsgDto);
        var result = streamBridge.send("sendCommunication-out-0",accountsMsgDto);
        log.info("Is the Communication request successfully processed?: {}",result);
    }

    @GetMapping
    public Flux<InterviewSlotResponse> geAllSlots(){
        return slotService.getAllSlots();
    }

    @GetMapping("/available")
    public Flux<InterviewSlotResponse> getAvailableSlots() {
        return slotService.getAvailableSlots();
    }


    @GetMapping("/interview/{interviewId}")
    public Flux<InterviewSlotResponse> getSlotsByInterview(@PathVariable("interviewId") Integer interviewId) {
        return slotService.getSlotsByInterviewId(interviewId,interviewerId);
    }


    @PostMapping("/{slotId}/cancel")
    public Mono<InterviewSlotResponse> cancelSlot(
            @PathVariable("slotId") Integer slotId,
            @RequestParam("cancelledBy") Long cancelledBy,
            @RequestParam("reason") String reason) {
        return slotService.cancelSlot(slotId, interviewerId,cancelledBy, reason);
    }

}
