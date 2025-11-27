package com.itsutra.project.ai.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class HelloController {


    @GetMapping("/")
    public String helloWorld(){
        return "Hello World";
    }
}
