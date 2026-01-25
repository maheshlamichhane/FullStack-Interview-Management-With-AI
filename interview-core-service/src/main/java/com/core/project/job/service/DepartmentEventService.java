package com.core.project.job.service;

import com.core.project.job.dto.DepartmentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;


@Service
public class DepartmentEventService {

    private final Sinks.Many<DepartmentEvent> sink = Sinks.many().multicast().onBackpressureBuffer();
    private final Flux<DepartmentEvent> flux = sink.asFlux().cache(0);

    public void emitEvent(DepartmentEvent event){
        this.sink.tryEmitNext(event);
    }

    public Flux<DepartmentEvent> subscribe(){
        return this.flux;
    }

}
