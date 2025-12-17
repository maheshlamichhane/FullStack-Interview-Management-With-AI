package com.itsutra.project.interview.controller;


import com.itsutra.project.config.AppProperties;
import com.itsutra.project.interview.dto.InterviewRequest;
import com.itsutra.project.interview.dto.InterviewResponse;
import com.itsutra.project.interview.dto.InterviewUpdateRequest;
import com.itsutra.project.interview.enums.InterviewStatus;
import com.itsutra.project.interview.service.InterviewService;
import com.itsutra.project.interview.service.client.InterviewAIFeignClient;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;
    private final AppProperties appProperties;
    private final InterviewAIFeignClient interviewAIFeignClient;


    @GetMapping("/build-version")
    @Retry(name="getBuildVersion",fallbackMethod = "getBuildVersionFallback")
    public String getBuildVersion(){
        return appProperties.getVersion();
//        throw new RuntimeException("getBuildVersion");
    }

    public String getBuildVersionFallback(Throwable throwable){
        return "0.9";
    }




    @RateLimiter(name = "sayHello",fallbackMethod = "getJavaVersionFallback")
    @GetMapping("/sayHello")
    public String sayHello() {
        return "Hello from Interview AND "
                + interviewAIFeignClient.sayHello();
    }

    public ResponseEntity<String> getJavaVersionFallback(Throwable throwable){
        return ResponseEntity.status(HttpStatus.OK).body("Fallback Message");
    }


    @PostMapping
    public ResponseEntity<InterviewResponse> createInterview(@Valid @RequestBody InterviewRequest request) throws Exception {
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
    public ResponseEntity<List<InterviewResponse>> getInterviewsByStatus(
            @PathVariable InterviewStatus status,
            Pageable pageable) {
        List<InterviewResponse> responses = interviewService.getInterviewsByStatus(status);
        return ResponseEntity.ok(responses);
    }

    @PutMapping
    public ResponseEntity<InterviewResponse> updateInterview(
            @Valid @RequestBody InterviewUpdateRequest request) {
        InterviewResponse response = interviewService.updateInterview(request);
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
