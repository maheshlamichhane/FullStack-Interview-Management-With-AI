package com.ai.project.dao;//package com.itsutra.ai.project.dao;
//
//import com.itsutra.ai.project.entity.InterviewAnalysis;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//@Repository
//public interface InterviewAnalysisRepository extends JpaRepository<InterviewAnalysis, UUID> {
//
//    Optional<InterviewAnalysis> findByInterviewId(UUID interviewId);
//
//    List<InterviewAnalysis> findByInterviewIdIn(List<UUID> interviewIds);
//
//    @Query("SELECT ia FROM InterviewAnalysis ia WHERE ia.overallScore >= :minScore")
//    List<InterviewAnalysis> findByMinScore(@Param("minScore") BigDecimal minScore);
//}
