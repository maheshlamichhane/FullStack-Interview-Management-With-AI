package com.itsutra.project.controller;


import com.itsutra.project.dto.InterviewSlotRequest;
import com.itsutra.project.dto.InterviewSlotResponse;
import com.itsutra.project.dto.SlotBookingRequest;
import com.itsutra.project.service.InterviewSlotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interview-slots")
@RequiredArgsConstructor
public class InterviewSlotController {

    private final InterviewSlotService slotService;

    @PostMapping("/interview/{interviewId}")
    public ResponseEntity<InterviewSlotResponse> createSlot(
            @PathVariable Long interviewId,
            @Valid @RequestBody InterviewSlotRequest request) {
        InterviewSlotResponse response = slotService.createSlot(request, interviewId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/interviewer/{interviewerId}/available")
    public ResponseEntity<List<InterviewSlotResponse>> getAvailableSlots(@PathVariable Long interviewerId) {
        List<InterviewSlotResponse> responses = slotService.getAvailableSlotsByInterviewer(interviewerId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/interview/{interviewId}")
    public ResponseEntity<List<InterviewSlotResponse>> getSlotsByInterview(@PathVariable Long interviewId) {
        List<InterviewSlotResponse> responses = slotService.getSlotsByInterview(interviewId);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/book")
    public ResponseEntity<InterviewSlotResponse> bookSlot(@Valid @RequestBody SlotBookingRequest request) {
        InterviewSlotResponse response = slotService.bookSlot(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{slotId}/cancel")
    public ResponseEntity<InterviewSlotResponse> cancelSlot(
            @PathVariable Long slotId,
            @RequestParam Long cancelledBy,
            @RequestParam String reason) {
        InterviewSlotResponse response = slotService.cancelSlot(slotId, cancelledBy, reason);
        return ResponseEntity.ok(response);
    }
}
