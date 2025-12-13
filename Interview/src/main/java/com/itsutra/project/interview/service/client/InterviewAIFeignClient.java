package com.itsutra.project.interview.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="interviews-ai",fallback = InterviewAIFeignClientFallback.class)
public interface InterviewAIFeignClient {

    @GetMapping("/sayHello")
    String sayHello();

}
