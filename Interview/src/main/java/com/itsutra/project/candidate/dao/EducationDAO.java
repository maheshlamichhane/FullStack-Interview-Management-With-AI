package com.itsutra.project.candidate.dao;


import com.itsutra.project.candidate.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationDAO extends JpaRepository<Education, Long> {
    List<Education> findByCandidateId(Long candidateId);
    List<Education> findByCandidateIdAndIsCurrent(Long candidateId, Boolean isCurrent);
    void deleteByCandidateId(Long candidateId);
}
