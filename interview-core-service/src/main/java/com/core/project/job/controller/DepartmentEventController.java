package com.core.project.job.controller;


import com.core.project.job.dto.DepartmentEvent;
import com.core.project.job.service.DepartmentEventService;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
@AllArgsConstructor
public class DepartmentEventController {

    private final DepartmentEventService departmentEventService;

    @SubscriptionMapping
    public Flux<DepartmentEvent> departmentsEvents(){
        return this.departmentEventService.subscribe();
    }
}
