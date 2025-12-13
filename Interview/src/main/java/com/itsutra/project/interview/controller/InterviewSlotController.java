package com.itsutra.project.interview.controller;


import com.itsutra.project.interview.dto.InterviewSlotRequest;
import com.itsutra.project.interview.dto.InterviewSlotResponse;
import com.itsutra.project.interview.service.InterviewSlotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interview-slots")
@RequiredArgsConstructor
public class InterviewSlotController {

    private final InterviewSlotService slotService;
    private final Long interviewerId = 567284l;


    @PostMapping
    public ResponseEntity<InterviewSlotResponse> createSlot(
            @Valid @RequestBody InterviewSlotRequest request) {
        InterviewSlotResponse response = slotService.createSlot(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/available")
    public ResponseEntity<List<InterviewSlotResponse>> getAvailableSlots() {
        List<InterviewSlotResponse> responses = slotService.getAvailableSlots();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/interview/{interviewId}")
    public ResponseEntity<List<InterviewSlotResponse>> getSlotsByInterview(@PathVariable Long interviewId) {
        List<InterviewSlotResponse> responses = slotService.getSlotsByInterviewId(interviewId);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{slotId}/cancel")
    public ResponseEntity<InterviewSlotResponse> cancelSlot(
            @PathVariable Long slotId,
            @RequestParam Long cancelledBy,
            @RequestParam String reason) throws Exception {
        InterviewSlotResponse response = slotService.cancelSlot(slotId, cancelledBy, reason);
        return ResponseEntity.ok(response);
    }
}
