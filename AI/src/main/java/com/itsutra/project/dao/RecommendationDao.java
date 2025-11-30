package com.itsutra.project.dao;

import com.itsutra.project.entity.Recommendation;
import com.itsutra.project.enums.RecommendationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationDao extends JpaRepository<Recommendation, Long> {

    List<Recommendation> findByCandidateId(Long candidateId);
    List<Recommendation> findByJobPositionId(Long jobPositionId);
    List<Recommendation> findByRecommendationType(RecommendationType type);
    List<Recommendation> findByIsImplemented(Boolean isImplemented);
}
