package com.itsutra.project.interview.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="interviews-ai",url = "http://interviews-ai:8081", fallback = InterviewAIFeignClientFallback.class)
public interface InterviewAIFeignClient {

    @GetMapping("/sayHello")
    String sayHello();

}
