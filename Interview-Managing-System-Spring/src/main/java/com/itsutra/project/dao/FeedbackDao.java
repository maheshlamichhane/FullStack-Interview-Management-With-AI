package com.itsutra.project.dao;

import com.itsutra.project.entity.InterviewFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedbackDao extends JpaRepository<InterviewFeedback,Long> {

    @Query("SELECT f FROM InterviewFeedback f LEFT JOIN FETCH f.participant WHERE f.interview.id IN :interviewIds")
    List<InterviewFeedback> findByInterviewIdIn(@Param("interviewIds") List<Long> interviewIds);

    @Query("SELECT f FROM InterviewFeedback f LEFT JOIN FETCH f.participant WHERE f.interview.id = :interviewId and f.participant.id = :participantId")
    List<InterviewFeedback> getByInterviewIdAndParticipationId(@Param("interviewId") Long interviewId,@Param("participantId") Long participantId);
}
