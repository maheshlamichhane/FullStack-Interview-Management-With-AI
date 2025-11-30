package com.itsutra.project.dao;


import com.itsutra.project.entity.SkillMatching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillMatchingRepository extends JpaRepository<SkillMatching, Long> {

    Optional<SkillMatching> findByCandidateIdAndJobPositionId(Long candidateId, Long jobPositionId);
    List<SkillMatching> findByCandidateId(Long candidateId);
    List<SkillMatching> findByJobPositionId(Long jobPositionId);

    @Query("SELECT sm FROM SkillMatching sm WHERE sm.matchPercentage >= :minPercentage")
    List<SkillMatching> findByMatchPercentageGreaterThanEqual(@Param("minPercentage") Double minPercentage);

    @Query("SELECT AVG(sm.matchPercentage) FROM SkillMatching sm WHERE sm.jobPositionId = :jobPositionId")
    Double findAverageMatchPercentageByJobPosition(@Param("jobPositionId") Long jobPositionId);
}
