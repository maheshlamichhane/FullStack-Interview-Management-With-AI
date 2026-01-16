package com.ai.project.controller;


import com.ai.project.config.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Random;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class DemoController {

    private final AppProperties appProperties;
    private final Random random = new Random();

    private int errorCode = 500;

    @GetMapping
    public Mono<String> getBuildVersion() {
        return Mono.just(appProperties.getVersion());
    }

    @GetMapping("/sayHello")
    public Mono<String> sayHello() {
        return Mono.just("Hello from AI V2");
    }


    @GetMapping("/demo")
    public Mono<ResponseEntity<String>> getAIResponse() {
        return Mono.fromSupplier(() -> {
            System.out.println("########### Inside the get method ######");

            boolean success = random.nextBoolean();
            if (success) {
                System.out.println("Success");
                return ResponseEntity.ok("Hello from Random AI");
            } else {
                System.out.println("Error occurred");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Simulated error from AI");
            }
        });
    }

    @PostMapping("/demo/retry")
    public Mono<ResponseEntity<String>> retry() {
        return Mono.fromSupplier(() -> {
            System.out.println("########### Inside the post method ######");
            return ResponseEntity.status(errorCode)
                    .body("Retry injected: " + errorCode);
        });
    }

    @GetMapping("/analyze")
    public Mono<ResponseEntity<String>> analyze(@RequestParam String text) {
        return Mono.just(
                ResponseEntity.ok("AI analysis for V3: " + text)
        );
    }

    public Mono<ResponseEntity<String>> fallbackAIResponse(Throwable ex) {
        return Mono.just(
                ResponseEntity.ok("AI service is temporarily unavailable. Please try later.")
        );
    }
}
