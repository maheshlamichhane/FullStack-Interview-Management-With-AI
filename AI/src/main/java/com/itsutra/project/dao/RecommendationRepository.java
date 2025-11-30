package com.itsutra.project.dao;


import com.itsutra.project.entity.Recommendation;
import com.itsutra.project.enums.RecommendationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    List<Recommendation> findByCandidateId(Long candidateId);
    List<Recommendation> findByInterviewId(Long interviewId);
    List<Recommendation> findByRecommendationType(RecommendationType type);
    List<Recommendation> findByPriorityLevel(String priorityLevel);

    @Query("SELECT r FROM Recommendation r WHERE r.isImplemented = false AND r.confidenceScore > :minConfidence")
    Page<Recommendation> findPendingHighConfidenceRecommendations(@Param("minConfidence") Double minConfidence,
                                                                  Pageable pageable);

    @Query("SELECT r FROM Recommendation r WHERE r.candidateId = :candidateId AND r.isImplemented = false")
    List<Recommendation> findPendingRecommendationsByCandidate(@Param("candidateId") Long candidateId);
}
