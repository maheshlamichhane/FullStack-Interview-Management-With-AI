package com.core.project.interview.service.client;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class InterviewAIClient {

    private final WebClient webClient;

    public InterviewAIClient(WebClient interviewAiWebClient) {
        this.webClient = interviewAiWebClient;
    }

    public String analyzeInterview(String text) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/ai/analyze")
                        .queryParam("text", text)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}

