package com.itsutra.project.dao;

import com.itsutra.project.entity.Interview;
import com.itsutra.project.enums.InterviewStatus;
import com.itsutra.project.enums.InterviewType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;



@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {

    List<Interview> findByCandidateId(Long candidateId);
    List<Interview> findByInterviewerId(Long interviewerId);
    List<Interview> findByJobPositionId(Long jobPositionId);

    Page<Interview> findByStatus(InterviewStatus status, Pageable pageable);
    Page<Interview> findByInterviewType(InterviewType type, Pageable pageable);

    @Query("SELECT i FROM Interview i WHERE i.candidateId = :candidateId AND i.status = :status")
    List<Interview> findByCandidateIdAndStatus(@Param("candidateId") Long candidateId,
                                               @Param("status") InterviewStatus status);

    @Query("SELECT i FROM Interview i WHERE i.interviewerId = :interviewerId AND i.status = :status")
    List<Interview> findByInterviewerIdAndStatus(@Param("interviewerId") Long interviewerId,
                                                 @Param("status") InterviewStatus status);

    @Query("SELECT i FROM Interview i WHERE i.scheduledStartTime BETWEEN :startDate AND :endDate")
    List<Interview> findInterviewsBetweenDates(@Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate);

    @Query("SELECT i FROM Interview i WHERE i.interviewerId = :interviewerId AND i.scheduledStartTime BETWEEN :start AND :end")
    List<Interview> findInterviewerSchedule(@Param("interviewerId") Long interviewerId,
                                            @Param("start") LocalDateTime start,
                                            @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(i) FROM Interview i WHERE i.candidateId = :candidateId AND i.status = 'COMPLETED'")
    Long countCompletedInterviewsByCandidate(@Param("candidateId") Long candidateId);

    Optional<Interview> findByIdAndCandidateId(Long id, Long candidateId);
    Optional<Interview> findByIdAndInterviewerId(Long id, Long interviewerId);
}
