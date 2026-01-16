package com.core.project.interview.dao;//package com.itsutra.project.interview.dao;
//
//import com.itsutra.project.interview.entity.Interview;
//import com.itsutra.project.interview.enums.InterviewStatus;
//import com.itsutra.project.interview.enums.InterviewType;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.r2dbc.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.data.repository.reactive.ReactiveCrudRepository;
//import org.springframework.stereotype.Repository;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//
//
//@Repository
//public interface InterviewRepository extends ReactiveCrudRepository<Interview, Long> {
//
//
//    Flux<Interview> findByCandidateId(Long candidateId);
//    Flux<Interview> findByInterviewerId(Long interviewerId);
//
//    Mono<Interview> findByIdAndParticipantsParticipantId(Long interviewId, Long participantId);
//
//    Flux<Interview> findByJobPositionId(Long jobPositionId);
//
//    Page<Interview> findByStatus(InterviewStatus status, Pageable pageable);
//    Page<Interview> findByInterviewType(InterviewType type, Pageable pageable);
//
//    @Query("SELECT i FROM Interview i WHERE i.candidateId = :candidateId AND i.status = :status")
//    Flux<Interview> findByCandidateIdAndStatus(@Param("candidateId") Long candidateId,
//                                               @Param("status") InterviewStatus status);
//
//    @Query("SELECT i FROM Interview i WHERE i.interviewerId = :interviewerId AND i.status = :status")
//    Flux<Interview> findByInterviewerIdAndStatus(@Param("interviewerId") Long interviewerId,
//                                                 @Param("status") InterviewStatus status);
//
//    @Query("SELECT i FROM Interview i WHERE i.scheduledStartTime BETWEEN :startDate AND :endDate")
//    Flux<Interview> findInterviewsBetweenDates(@Param("startDate") LocalDateTime startDate,
//                                               @Param("endDate") LocalDateTime endDate);
//
//    @Query("SELECT i FROM Interview i WHERE i.interviewerId = :interviewerId AND i.scheduledStartTime BETWEEN :start AND :end")
//    Flux<Interview> findInterviewerSchedule(@Param("interviewerId") Long interviewerId,
//                                            @Param("start") LocalDateTime start,
//                                            @Param("end") LocalDateTime end);
//
////    @Query("SELECT COUNT(i) FROM Interview i WHERE i.candidateId = :candidateId AND i.status = com.itsutra.project.enums.InterviewStatus.COMPLETED AND i.interviewerId = :interviewerId")
////    Long countCompletedInterviewsByCandidateAndInterviewerId(@Param("candidateId") Long candidateId,@Param("interviewerId") Long interviewerId);
//
//    Mono<Long> countByInterviewerIdAndCandidateIdAndStatus(Long interviewerId, Long candidateId,InterviewStatus status);
//
//    Mono<Interview> findByIdAndCandidateId(Long id, Long candidateId);
//    Mono<Interview> findByIdAndInterviewerId(Long id, Long interviewerId);
//    Flux<Interview> findByStatusAndInterviewerId(InterviewStatus status, Long interviewerId);
//    Flux<Interview> findByCandidateIdAndInterviewerId(Long candidateId, Long interviewerId);
//
//    //from her
//    Mono<Boolean> existsByIdAndInterviewerId(Long id, Long interviewerId);
//}
