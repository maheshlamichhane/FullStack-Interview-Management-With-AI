package com.core.project.interview.controller;

import com.core.project.interview.dto.InterviewRequestDTO;
import com.core.project.interview.service.InterviewAiGrpcService;
import com.interview.project.proto.InterviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
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

    @GetMapping("/server-streaming")
    public Flux<String> getServerStreaming(){
        return interviewAiGrpcService.getServerStreamingData();
    }

}
