package com.core.project.job.controller;

import com.core.project.job.dto.JobDTO;
import com.core.project.job.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
public class JobController {

    @Autowired
    private JobService jobService;

    @QueryMapping
    public Flux<JobDTO> findAllJob(){
        return jobService.findAllJobs();
    }

//    @QueryMapping
//    public Flux<JobDTO> findAllJobByDepartmentId(@Argument("id") Long id){
//        return jobService.findAllJobByDepartmentId(id);
//    }
}
