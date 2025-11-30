package com.itsutra.project.dao;

import com.itsutra.project.entity.AIRequest;
import com.itsutra.project.enums.RequestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AIRequestDao extends JpaRepository<AIRequest, Long> {

    List<AIRequest> findBySessionId(String sessionId);
    List<AIRequest> findByCandidateId(Long candidateId);
    List<AIRequest> findByInterviewId(Long interviewId);
    List<AIRequest> findByRequestType(RequestType requestType);

    @Query("SELECT ar FROM AIRequest ar WHERE ar.createdAt BETWEEN :startDate AND :endDate")
    List<AIRequest> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(ar) FROM AIRequest ar WHERE ar.modelUsed = :model")
    Long countByModelUsed(@Param("model") String model);
}
