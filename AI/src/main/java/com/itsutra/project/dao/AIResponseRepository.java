package com.itsutra.project.dao;

import com.itsutra.project.entity.AIResponse;
import com.itsutra.project.enums.RequestType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AIResponseRepository extends JpaRepository<AIResponse, Long> {

    List<AIResponse> findByInterviewId(Long interviewId);
    List<AIResponse> findByCandidateId(Long candidateId);
    List<AIResponse> findByResponseType(RequestType responseType);

    @Query("SELECT ar FROM AIResponse ar WHERE ar.createdAt BETWEEN :startDate AND :endDate")
    List<AIResponse> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                     @Param("endDate") LocalDateTime endDate);

    @Query("SELECT ar FROM AIResponse ar WHERE ar.confidenceScore > :minConfidence")
    Page<AIResponse> findByHighConfidence(@Param("minConfidence") Double minConfidence, Pageable pageable);
}
