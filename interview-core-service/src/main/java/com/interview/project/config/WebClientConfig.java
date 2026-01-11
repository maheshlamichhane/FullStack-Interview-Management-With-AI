package com.interview.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient interviewAiWebClient() {
        return WebClient.builder()
                .baseUrl("http://interview-ai-backend.dev.svc.cluster.local:8081")
                .build();
    }


}
