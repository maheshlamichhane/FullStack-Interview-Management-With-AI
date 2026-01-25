package com.core.project.job.service;

import com.core.project.job.dao.JobDAO;
import com.core.project.job.dto.JobDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class JobService {

    @Autowired
    private JobDAO jobDAO;

    public Flux<JobDTO> findAllJobs(){
        return jobDAO.findAll()
                .map(job -> new JobDTO(job.getId(),job.getName(),job.getDepartmentId()));
    }

    public Flux<JobDTO> findAllJobByDepartmentId(List<Long> list){
        return jobDAO.findAllByDepartmentIdIn(list)
                .map(job -> new JobDTO(job.getId(),job.getName(),job.getDepartmentId()));
    }
}
