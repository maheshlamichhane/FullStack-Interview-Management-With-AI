package com.itsutra.project.dao;

import com.itsutra.project.entity.InterviewParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewParticipantRepository extends JpaRepository<InterviewParticipant, Long> {

    List<InterviewParticipant> findByInterviewId(Long interviewId);
    List<InterviewParticipant> findByParticipantId(Long participantId);

    @Query("SELECT ip FROM InterviewParticipant ip WHERE ip.interview.id = :interviewId AND ip.role = 'CANDIDATE'")
    Optional<InterviewParticipant> findCandidateByInterview(@Param("interviewId") Long interviewId);

    @Query("SELECT ip FROM InterviewParticipant ip WHERE ip.interview.id = :interviewId AND ip.role = 'INTERVIEWER'")
    List<InterviewParticipant> findInterviewersByInterview(@Param("interviewId") Long interviewId);

    @Query("SELECT ip FROM InterviewParticipant ip WHERE ip.interview.id = :interviewId AND ip.participantId = :participantId")
    Optional<InterviewParticipant> findByInterviewAndParticipant(@Param("interviewId") Long interviewId,
                                                                 @Param("participantId") Long participantId);

    @Query("SELECT COUNT(ip) FROM InterviewParticipant ip WHERE ip.interview.id = :interviewId AND ip.attended = true")
    Long countAttendedParticipantsByInterview(@Param("interviewId") Long interviewId);
}
