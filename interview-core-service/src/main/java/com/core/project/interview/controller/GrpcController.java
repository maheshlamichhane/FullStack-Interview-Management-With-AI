package com.core.project.interview.controller;

import com.core.project.interview.dto.InterviewRequestDTO;
import com.core.project.interview.dto.InterviewResponseDTO;
import com.core.project.interview.service.InterviewAiGrpcService;
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
    public Mono<InterviewResponseDTO> getAiData(@RequestBody InterviewRequestDTO request){
        return interviewAiGrpcService.getAiInformation(request);
    }

    @GetMapping("/server-streaming")
    public Flux<String> getServerStreaming(){
        return interviewAiGrpcService.getServerStreamingData();
    }

    @GetMapping("/client-streaming")
    public Mono<InterviewResponseDTO> getClientStreaming(@RequestParam("years")  int years){
        return interviewAiGrpcService.performClientStreaming(years);
    }



}
