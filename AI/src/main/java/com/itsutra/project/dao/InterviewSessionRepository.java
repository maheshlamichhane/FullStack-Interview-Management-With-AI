//package com.itsutra.project.dao;
//
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface InterviewSessionRepository extends JpaRepository<InterviewSession, Long> {
//
//    Optional<InterviewSession> findByInterviewId(Long interviewId);
//    List<InterviewSession> findByCandidateId(Long candidateId);
//    List<InterviewSession> findByInterviewerId(Long interviewerId);
//
//    @Query("SELECT is FROM InterviewSession is WHERE is.isAnalysisComplete = false")
//    List<InterviewSession> findSessionsPendingAnalysis();
//
//    @Query("SELECT is FROM InterviewSession is WHERE is.overallSentimentScore < :sentimentThreshold")
//    List<InterviewSession> findSessionsWithLowSentiment(@Param("sentimentThreshold") Double sentimentThreshold);
//}