package com.itsutra.project.dao;


import com.itsutra.project.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationDAO extends JpaRepository<Education, Long> {
    List<Education> findByCandidateId(Long candidateId);
    List<Education> findByCandidateIdAndIsCurrent(Long candidateId, Boolean isCurrent);
    void deleteByCandidateId(Long candidateId);
}
