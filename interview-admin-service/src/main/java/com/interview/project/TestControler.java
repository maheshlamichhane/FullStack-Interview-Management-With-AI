package com.interview.project;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/admin")
public class TestControler {

    @GetMapping("/sayHello")
    public Flux<String> getList(){
        return Flux.just("Hello","World","Hellow Baby");
    }

}
