package com.itsutra.ai.project.controller;

import com.itsutra.ai.project.config.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class DemoController {

    private final AppProperties appProperties;


    @GetMapping
    public String getBuildVersion(){
        return appProperties.getVersion();
    }

    @GetMapping("/sayHello")
    public String sayHello(){
        return "Hello from AI";
    }



}
