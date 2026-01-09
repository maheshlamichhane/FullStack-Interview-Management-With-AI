package com.itsutra.project.interview.dao;


import com.itsutra.project.interview.entity.InterviewSlot;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


@Repository
public interface InterviewSlotRepository extends ReactiveCrudRepository<InterviewSlot, Long> {

    @Query("SELECT *  FROM interview_slots  WHERE interview_id =:interviewId AND interviewer_id =:interviewerId")
    Flux<InterviewSlot> findByInterviewIdAndInterviewerId(Integer interviewId, Long interviewerId);

//
//
//    Flux<InterviewSlot> findByInterviewerId(Long interviewerId);
//    Flux<InterviewSlot> findByStatus(SlotStatus status);
//


    @Query("""
    SELECT *
    FROM interview_slots
    WHERE interviewer_id = :interviewerId
    AND status = 'AVAILABLE'
    """)
    Flux<InterviewSlot> findAvailableSlotsByInterviewer(Long interviewerId);


    @Query("""
        SELECT * FROM interview_slots
        WHERE interviewer_id = :interviewerId
        AND start_time  =:end
        AND end_time =:start
        """)
    Flux<InterviewSlot> findConflictingSlots( Long interviewerId,
            @RequestParam("start") LocalDateTime start,
            @RequestParam("end")  LocalDateTime end
    );


    @Query("""
        SELECT * FROM interview_slots
        WHERE interviewer_id = :interviewerId
        AND id = :slotId
        """)
    Mono<InterviewSlot> findSlotBySlotIdAndInterviewerId( Long interviewerId,Integer slotId);


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