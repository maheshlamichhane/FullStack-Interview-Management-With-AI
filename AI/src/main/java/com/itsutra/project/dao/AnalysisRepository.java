package com.itsutra.project.dao;

import com.itsutra.project.entity.Analysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnalysisRepository extends JpaRepository<Analysis, Long> {


    List<Analysis> findByInterviewId(Long interviewId);
    List<Analysis> findByCandidateId(Long candidateId);
    List<Analysis> findByAnalysisType(Analysis.AnalysisType analysisType);

    @Query("SELECT a FROM Analysis a WHERE a.interviewId = :interviewId AND a.analysisType = :analysisType")
    Optional<Analysis> findByInterviewAndType(@Param("interviewId") Long interviewId,
                                              @Param("analysisType") Analysis.AnalysisType analysisType);

    @Query("SELECT a FROM Analysis a WHERE a.sentimentScore < :sentimentThreshold")
    List<Analysis> findNegativeSentimentAnalyses(@Param("sentimentThreshold") Double sentimentThreshold);
}
