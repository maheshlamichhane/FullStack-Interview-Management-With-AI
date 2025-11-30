package com.itsutra.project.controller;


import com.itsutra.project.dto.FeedbackRequest;
import com.itsutra.project.dto.FeedbackResponse;
import com.itsutra.project.dto.FeedbackUpdateRequest;
import com.itsutra.project.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<FeedbackResponse> createFeedback(@Valid @RequestBody FeedbackRequest request) {
        FeedbackResponse response = feedbackService.createFeedback(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/interview/{interviewId}")
    public ResponseEntity<List<FeedbackResponse>> getFeedbacksByInterview(@PathVariable Long interviewId) {
        List<FeedbackResponse> responses = feedbackService.getFeedbacksByInterview(interviewId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<FeedbackResponse>> getFeedbacksByProvider(@PathVariable Long providerId) {
        List<FeedbackResponse> responses = feedbackService.getFeedbacksByProvider(providerId);
        return ResponseEntity.ok(responses);
    }


    @GetMapping("/interview/{interviewId}/final")
    public ResponseEntity<List<FeedbackResponse>> getFinalFeedback(@PathVariable Long interviewId) {
        List<FeedbackResponse> response = feedbackService.getFinalFeedbackByInterview(interviewId);
        return ResponseEntity.ok(response);
    }


    @PutMapping
    public ResponseEntity<FeedbackResponse> updateFeedback(
            @Valid @RequestBody FeedbackUpdateRequest request) {
        FeedbackResponse response = feedbackService.updateFeedback(request.getId(), request);
        return ResponseEntity.ok(response);
    }




    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/final")
    public ResponseEntity<FeedbackResponse> markAsFinalFeedback(@PathVariable Long id) {
        FeedbackResponse response = feedbackService.markAsFinalFeedback(id);
        return ResponseEntity.ok(response);
    }



    @PostMapping("/{id}/share")
    public ResponseEntity<FeedbackResponse> shareFeedbackWithCandidate(@PathVariable Long id) {
        FeedbackResponse response = feedbackService.shareFeedbackWithCandidate(id);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/candidate/{candidateId}/average-rating")
    public ResponseEntity<Double> getCandidateAverageRating(@PathVariable Long candidateId) {
        Double averageRating = feedbackService.getCandidateAverageRating(candidateId);
        return ResponseEntity.ok(averageRating);
    }
}
