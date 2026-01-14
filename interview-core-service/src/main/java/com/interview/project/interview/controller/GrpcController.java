package com.interview.project.interview.controller;

import com.interview.project.interview.dto.InterviewRequestDTO;
import com.interview.project.interview.service.InterviewAiGrpcService;
import com.interview.project.proto.InterviewRequest;
import com.interview.project.proto.InterviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class GrpcController {

    private final InterviewAiGrpcService interviewAiGrpcService;

    @PostMapping("/unary")
    public Mono<InterviewResponse> getAiData(@RequestBody InterviewRequestDTO request){
        return interviewAiGrpcService.getAiInformation(request);
    }





}
