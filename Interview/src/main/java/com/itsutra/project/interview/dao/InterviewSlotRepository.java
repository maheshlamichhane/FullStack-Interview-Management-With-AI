package com.itsutra.project.interview.dao;


import com.itsutra.project.interview.entity.InterviewSlot;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;


@Repository
public interface InterviewSlotRepository extends ReactiveCrudRepository<InterviewSlot, Long> {

//    @Query("SELECT s FROM InterviewSlot s WHERE s.interview.id = :interviewId AND s.interviewerId = :interviewerId")
//    Mono<InterviewSlot> findByInterviewIdAndInterviewerId(Long interviewId, Long interviewerId);
//
//
//    Flux<InterviewSlot> findByInterviewerId(Long interviewerId);
//    Flux<InterviewSlot> findByStatus(SlotStatus status);
//
//    @Query("SELECT s FROM InterviewSlot s WHERE s.interviewerId = :interviewerId AND s.status = 'AVAILABLE' AND s.startTime > :now")
//    Flux<InterviewSlot> findAvailableSlotsByInterviewer(@Param("interviewerId") Long interviewerId,
//                                                        @Param("now") LocalDateTime now);
//
    @Query("""
        SELECT * FROM interview_slots s
        WHERE s.interviewer_id = :interviewerId
        AND s.start_time  =:end
        AND s.end_time =:start
        """)
    Flux<InterviewSlot> findConflictingSlots(@RequestParam("interviewerId") Long interviewerId,
            @RequestParam("start") LocalDateTime start,
            @RequestParam("end")  LocalDateTime end
    );
//
//
//    @Query("SELECT s FROM InterviewSlot s WHERE s.interviewerId = :interviewerId AND s.startTime = :startTime")
//    Mono<InterviewSlot> findByInterviewerAndStartTime(@Param("interviewerId") Long interviewerId,
//                                                          @Param("startTime") LocalDateTime startTime);
//
//    @Query("SELECT COUNT(s) FROM InterviewSlot s WHERE s.interviewerId = :interviewerId AND s.status = 'BOOKED' AND s.startTime BETWEEN :start AND :end")
//    Mono<Long> countBookedSlotsByInterviewerInPeriod(@Param("interviewerId") Long interviewerId,
//                                               @Param("start") LocalDateTime start,
//                                               @Param("end") LocalDateTime end);
}