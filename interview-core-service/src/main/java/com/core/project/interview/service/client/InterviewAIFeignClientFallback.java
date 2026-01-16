package com.core.project.interview.service.client;

import org.springframework.stereotype.Component;

@Component
public class InterviewAIFeignClientFallback  implements InterviewAIFeignClient {


    @Override
    public String sayHello() {
        return "Error Occured while sending the request to interview ai. interview ai is down";
    }
}
