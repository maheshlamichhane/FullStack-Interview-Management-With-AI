package com.itsutra.project.interview.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="interviews-ai")
public interface InterviewAIFeignClient {

    @GetMapping("/sayHello")
    String sayHello();

}
