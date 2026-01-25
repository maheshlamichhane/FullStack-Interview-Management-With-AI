package com.core.project.job.dao;

import com.core.project.job.entity.Job;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface JobDAO extends ReactiveCrudRepository<Job,Long> {
    Flux<Job> findAllByDepartmentId(Long departmentId);
    Flux<Job> findAllByDepartmentIdIn(List<Long> departmentIds);
}
