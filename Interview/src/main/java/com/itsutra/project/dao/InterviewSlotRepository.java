package com.itsutra.project.dao;


import com.itsutra.project.entity.InterviewSlot;
import com.itsutra.project.enums.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewSlotRepository extends JpaRepository<InterviewSlot, Long> {

    List<InterviewSlot> findByInterviewId(Long interviewId);
    List<InterviewSlot> findByInterviewerId(Long interviewerId);
    List<InterviewSlot> findByStatus(SlotStatus status);

    @Query("SELECT s FROM InterviewSlot s WHERE s.interviewerId = :interviewerId AND s.status = 'AVAILABLE' AND s.startTime > :now")
    List<InterviewSlot> findAvailableSlotsByInterviewer(@Param("interviewerId") Long interviewerId,
                                                        @Param("now") LocalDateTime now);

    @Query("SELECT s FROM InterviewSlot s WHERE s.interviewerId = :interviewerId AND s.startTime BETWEEN :start AND :end")
    List<InterviewSlot> findSlotsByInterviewerAndTimeRange(@Param("interviewerId") Long interviewerId,
                                                           @Param("start") LocalDateTime start,
                                                           @Param("end") LocalDateTime end);

    @Query("SELECT s FROM InterviewSlot s WHERE s.interviewerId = :interviewerId AND s.startTime = :startTime")
    Optional<InterviewSlot> findByInterviewerAndStartTime(@Param("interviewerId") Long interviewerId,
                                                          @Param("startTime") LocalDateTime startTime);

    @Query("SELECT COUNT(s) FROM InterviewSlot s WHERE s.interviewerId = :interviewerId AND s.status = 'BOOKED' AND s.startTime BETWEEN :start AND :end")
    Long countBookedSlotsByInterviewerInPeriod(@Param("interviewerId") Long interviewerId,
                                               @Param("start") LocalDateTime start,
                                               @Param("end") LocalDateTime end);
}