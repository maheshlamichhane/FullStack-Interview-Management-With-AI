package com.ai.project.dao;//package com.itsutra.ai.project.dao;
//
//import com.itsutra.ai.project.entity.AIRequest;
//import com.itsutra.ai.project.enums.AIServiceType;
//import com.itsutra.ai.project.enums.RequestStatus;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//@Repository
//public interface AIRequestRepository extends JpaRepository<AIRequest, UUID> {
//
//    Optional<AIRequest> findByRequestId(String requestId);
//
//    List<AIRequest> findByServiceType(AIServiceType serviceType);
//
//    List<AIRequest> findByStatus(RequestStatus status);
//
//    List<AIRequest> findByUserId(UUID userId);
//
//    List<AIRequest> findByInterviewId(UUID interviewId);
//
//    List<AIRequest> findByCandidateId(UUID candidateId);
//
//    Page<AIRequest> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
//
//    @Query("SELECT COUNT(a) FROM AIRequest a WHERE a.serviceType = :serviceType AND a.status = 'COMPLETED'")
//    Long countCompletedByServiceType(@Param("serviceType") AIServiceType serviceType);
//
//    @Query("SELECT AVG(a.processingTimeMs) FROM AIRequest a WHERE a.status = 'COMPLETED'")
//    Double getAverageProcessingTime();
//}
