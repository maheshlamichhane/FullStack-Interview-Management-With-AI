package com.itsutra.project.dao;


import com.itsutra.project.entity.JobRequirement;
import com.itsutra.project.enums.RequirementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRequirementDAO extends JpaRepository<JobRequirement, Long> {
    List<JobRequirement> findByJobPositionId(Long jobPositionId);
    List<JobRequirement> findByJobPositionIdAndRequirementType(Long jobPositionId, RequirementType type);
    void deleteByJobPositionId(Long jobPositionId);
}
