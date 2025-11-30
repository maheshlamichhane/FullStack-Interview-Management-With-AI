package com.itsutra.project.dao;

import com.itsutra.project.entity.Analysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnalysisDao extends JpaRepository<Analysis, Long> {

    List<Analysis> findByCandidateId(Long candidateId);
    List<Analysis> findByInterviewId(Long interviewId);
    Optional<Analysis> findByCandidateIdAndInterviewIdAndAnalysisType(
            Long candidateId, Long interviewId, Analysis.AnalysisType analysisType);

    @Query("SELECT a FROM Analysis a WHERE a.sentimentScore > :minScore")
    List<Analysis> findBySentimentScoreGreaterThan(@Param("minScore") Double minScore);

    @Query("SELECT AVG(a.sentimentScore) FROM Analysis a WHERE a.candidateId = :candidateId")
    Double findAverageSentimentByCandidateId(@Param("candidateId") Long candidateId);
}
