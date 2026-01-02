package com.itsutra.ai.project.controller;

import com.itsutra.ai.project.config.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class DemoController {

    private final AppProperties appProperties;

    private int errorCode = 500;
    private final Random random = new Random();


    @GetMapping
    public String getBuildVersion(){
        return appProperties.getVersion();
    }

    @GetMapping("/sayHello")
    public String sayHello(){
        return "Hello from AI V2 ";
    }

    @GetMapping("/demo")
    public ResponseEntity<String> getAIResponse() {
        System.out.println("########### Inside the get method ######");

        // Randomly decide success or failure
        boolean success = random.nextBoolean(); // 50% chance
        if (success) {
            System.out.println("Success");
            return ResponseEntity.ok("Hello from Random AI");
        } else {
            System.out.println("Error occured");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Simulated error from AI");
        }
    }

    @PostMapping("/demo/retry")
    public ResponseEntity<String> retry(){
        System.out.println("###########  Inside the post method ######");
        return ResponseEntity.status(errorCode)
                    .body("Retry injected: " + errorCode);
    }

    @GetMapping("/analyze")
    public ResponseEntity<String> analyze(@RequestParam String text) {
        return ResponseEntity.ok("AI analysis for V3: " + text);
    }



    public ResponseEntity<String> fallbackAIResponse(Exception ex) {
        // This is returned after retries fail
        return ResponseEntity.ok("AI service is temporarily unavailable. Please try later.");
    }



}
