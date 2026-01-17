package com.core.project.interview.controller;

import com.core.project.interview.dto.InterviewRequestDTO;
import com.core.project.interview.dto.InterviewResponseDTO;
import com.core.project.interview.service.InterviewAiGrpcService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class GrpcController {

    private final InterviewAiGrpcService interviewAiGrpcService;

    @PostMapping(value = "/unary",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<InterviewResponseDTO> getAiData(@RequestBody InterviewRequestDTO request){
        return interviewAiGrpcService.getAiInformation(request);
    }

    @GetMapping(value = "/server-streaming",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getServerStreaming(){
        return interviewAiGrpcService.getServerStreamingData();
    }

    @GetMapping(value = "/client-streaming",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<InterviewResponseDTO> getClientStreaming(@RequestParam("years")  int years){
        return interviewAiGrpcService.performClientStreaming(years);
    }

    @GetMapping(value="/bidirectional-streaming",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<InterviewResponseDTO> getBidirectionalStreaming(@RequestParam("years")  int years){
        return interviewAiGrpcService.performBidirectionalStreaming(years);
    }



}
