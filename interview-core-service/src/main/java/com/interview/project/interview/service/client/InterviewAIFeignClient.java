package com.interview.project.interview.service.client;

import org.springframework.web.bind.annotation.GetMapping;


public interface InterviewAIFeignClient {

    @GetMapping("/sayHello")
    String sayHello();

}
