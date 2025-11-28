package com.itsutra.project.controller;


import com.itsutra.project.dto.InterviewRequest;
import com.itsutra.project.dto.InterviewResponse;
import com.itsutra.project.dto.InterviewUpdateRequest;
import com.itsutra.project.enums.InterviewStatus;
import com.itsutra.project.service.InterviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping
    public ResponseEntity<InterviewResponse> createInterview(@Valid @RequestBody InterviewRequest request) {
        InterviewResponse response = interviewService.createInterview(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InterviewResponse> getInterview(@PathVariable Long id) {
        InterviewResponse response = interviewService.getInterviewById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<List<InterviewResponse>> getInterviewsByCandidate(@PathVariable Long candidateId) {
        List<InterviewResponse> responses = interviewService.getInterviewsByCandidate(candidateId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/interviewer/{interviewerId}")
    public ResponseEntity<List<InterviewResponse>> getInterviewsByInterviewer(@PathVariable Long interviewerId) {
        List<InterviewResponse> responses = interviewService.getInterviewsByInterviewer(interviewerId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<InterviewResponse>> getInterviewsByStatus(
            @PathVariable InterviewStatus status,
            Pageable pageable) {
        Page<InterviewResponse> responses = interviewService.getInterviewsByStatus(status, pageable);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InterviewResponse> updateInterview(
            @PathVariable Long id,
            @Valid @RequestBody InterviewUpdateRequest request) {
        InterviewResponse response = interviewService.updateInterview(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInterview(@PathVariable Long id) {
        interviewService.deleteInterview(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<InterviewResponse> startInterview(@PathVariable Long id) {
        InterviewResponse response = interviewService.startInterview(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<InterviewResponse> completeInterview(@PathVariable Long id) {
        InterviewResponse response = interviewService.completeInterview(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<InterviewResponse> cancelInterview(
            @PathVariable Long id,
            @RequestParam String reason) {
        InterviewResponse response = interviewService.cancelInterview(id, reason);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/candidate/{candidateId}/count")
    public ResponseEntity<Long> getInterviewCountByCandidate(@PathVariable Long candidateId) {
        Long count = interviewService.getInterviewCountByCandidate(candidateId);
        return ResponseEntity.ok(count);
    }
}
