package com.itsutra.project.interview.dao;


import com.itsutra.project.interview.entity.InterviewParticipant;
import com.itsutra.project.interview.enums.ParticipantRole;
import com.itsutra.project.interview.enums.ParticipantType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewParticipantRepository extends JpaRepository<InterviewParticipant, Long> {

    // Basic CRUD operations are provided by JpaRepository

    // Find all participants for a specific interview
    List<InterviewParticipant> findByInterviewIdAndInterviewerId(Long interviewId, Long interviewerId);

    // Find all interviews for a specific participant
    List<InterviewParticipant> findByParticipantId(Long participantId);
    Optional<InterviewParticipant> findByParticipantIdAndInterviewerId(Long participantId, Long interviewerId);
    Optional<InterviewParticipant> findByIdAndInterviewerId(Long id, Long interviewerId);

    // Find participants by type
    List<InterviewParticipant> findByParticipantType(ParticipantType participantType);

    // Find participants by role
    List<InterviewParticipant> findByRole(ParticipantRole role);

    // Find specific participant in an interview
    Optional<InterviewParticipant> findByInterviewIdAndParticipantIdAndInterviewerId(Long interviewId, Long participantId,Long interviewerId);

    // Find participants by interview and role
    @Query("SELECT ip FROM InterviewParticipant ip WHERE ip.interview.id = :interviewId AND ip.role = :role")
    List<InterviewParticipant> findByInterviewIdAndRole(@Param("interviewId") Long interviewId,
                                                        @Param("role") ParticipantRole role);

    List<InterviewParticipant> findByInterviewIdAndParticipantIdAndRole(Long interviewId, Long participantId,ParticipantRole role);

    // Find participants by interview and participant type
    @Query("SELECT ip FROM InterviewParticipant ip WHERE ip.interview.id = :interviewId AND ip.participantType = :participantType")
    List<InterviewParticipant> findByInterviewIdAndParticipantType(@Param("interviewId") Long interviewId,
                                                                   @Param("participantType") ParticipantType participantType);

    // Count confirmed participants for an interview
    @Query("SELECT COUNT(ip) FROM InterviewParticipant ip WHERE ip.interview.id = :interviewId AND ip.confirmedAttendance = true")
    Long countConfirmedParticipantsByInterviewId(@Param("interviewId") Long interviewId);

    Long countConfirmedParticipantsByInterviewIdAndInterviewerIdAndConfirmedAttendanceTrue(Long interviewId, Long interviewerId);

    // Count required participants for an interview
    @Query("SELECT COUNT(ip) FROM InterviewParticipant ip WHERE ip.interview.id = :interviewId AND ip.isRequired = true")
    Long countRequiredParticipantsByInterviewId(@Param("interviewId") Long interviewId);

    // Count attended participants for an interview
    @Query("SELECT COUNT(ip) FROM InterviewParticipant ip WHERE ip.interview.id = :interviewId AND ip.attended = true")
    Long countAttendedParticipantsByInterviewId(@Param("interviewId") Long interviewId);

    // Check if participant exists in interview
    boolean existsByInterviewIdAndParticipantId(Long interviewId, Long participantId);


    boolean existsByParticipantIdAndInterviewIdAndInterviewerId(Long participantId, Long interviewId,Long interviewerId);

    // Check if participant has confirmed attendance
    @Query("SELECT ip.confirmedAttendance FROM InterviewParticipant ip WHERE ip.interview.id = :interviewId AND ip.participantId = :participantId")
    Optional<Boolean> findConfirmedAttendanceByInterviewIdAndParticipantId(@Param("interviewId") Long interviewId,
                                                                           @Param("participantId") Long participantId);

    // Delete participant from specific interview
    @Modifying
    @Query("DELETE FROM InterviewParticipant ip WHERE ip.interview.id = :interviewId AND ip.participantId = :participantId")
    void deleteByInterviewIdAndParticipantId(@Param("interviewId") Long interviewId,
                                             @Param("participantId") Long participantId);


    void deleteByInterviewIdAndParticipantIdAndInterviewerId(Long interviewId, Long participantId, Long interviewerId);

    // Delete all participants from an interview
    @Modifying
    @Query("DELETE FROM InterviewParticipant ip WHERE ip.interview.id = :interviewId")
    void deleteAllByInterviewId(@Param("interviewId") Long interviewId);

    // Find participants by multiple participant IDs
    List<InterviewParticipant> findByParticipantIdIn(List<Long> participantIds);

    // Find participants who haven't confirmed attendance
    @Query("SELECT ip FROM InterviewParticipant ip WHERE ip.interview.id = :interviewId AND ip.confirmedAttendance = false")
    List<InterviewParticipant> findUnconfirmedParticipantsByInterviewId(@Param("interviewId") Long interviewId);

    // Find participants who haven't attended
    @Query("SELECT ip FROM InterviewParticipant ip WHERE ip.interview.id = :interviewId AND ip.attended = false")
    List<InterviewParticipant> findNotAttendedParticipantsByInterviewId(@Param("interviewId") Long interviewId);

    // Find required participants for an interview
    @Query("SELECT ip FROM InterviewParticipant ip WHERE ip.interview.id = :interviewId AND ip.isRequired = true")
    List<InterviewParticipant> findRequiredParticipantsByInterviewId(@Param("interviewId") Long interviewId);

    // Find optional participants for an interview
    @Query("SELECT ip FROM InterviewParticipant ip WHERE ip.interview.id = :interviewId AND ip.isRequired = false")
    List<InterviewParticipant> findOptionalParticipantsByInterviewId(@Param("interviewId") Long interviewId);

    // Bulk update confirmation status
    @Modifying
    @Query("UPDATE InterviewParticipant ip SET ip.confirmedAttendance = :confirmed WHERE ip.id IN :ids")
    int bulkUpdateConfirmationStatus(@Param("ids") List<Long> ids, @Param("confirmed") Boolean confirmed);

    // Bulk update attendance status
    @Modifying
    @Query("UPDATE InterviewParticipant ip SET ip.attended = :attended WHERE ip.id IN :ids")
    int bulkUpdateAttendanceStatus(@Param("ids") List<Long> ids, @Param("attended") Boolean attended);

    // Find participants by interview and confirmation status
    @Query("SELECT ip FROM InterviewParticipant ip WHERE ip.interview.id = :interviewId AND ip.confirmedAttendance = :confirmed")
    List<InterviewParticipant> findByInterviewIdAndConfirmedAttendance(@Param("interviewId") Long interviewId,
                                                                       @Param("confirmed") Boolean confirmed);

    // Find participants by interview and attendance status
    @Query("SELECT ip FROM InterviewParticipant ip WHERE ip.interview.id = :interviewId AND ip.attended = :attended")
    List<InterviewParticipant> findByInterviewIdAndAttended(@Param("interviewId") Long interviewId,
                                                            @Param("attended") Boolean attended);

    // Find primary interviewers for an interview
    @Query("SELECT ip FROM InterviewParticipant ip WHERE ip.interview.id = :interviewId AND ip.role = 'PRIMARY_INTERVIEWER'")
    List<InterviewParticipant> findPrimaryInterviewersByInterviewId(@Param("interviewId") Long interviewId);

    // Find secondary interviewers for an interview
    @Query("SELECT ip FROM InterviewParticipant ip WHERE ip.interview.id = :interviewId AND ip.role = 'SECONDARY_INTERVIEWER'")
    List<InterviewParticipant> findSecondaryInterviewersByInterviewId(@Param("interviewId") Long interviewId);

    // Find observers for an interview
    @Query("SELECT ip FROM InterviewParticipant ip WHERE ip.interview.id = :interviewId AND ip.role = 'OBSERVER'")
    List<InterviewParticipant> findObserversByInterviewId(@Param("interviewId") Long interviewId);

    // Check if any required participant hasn't confirmed
    @Query("SELECT COUNT(ip) > 0 FROM InterviewParticipant ip WHERE ip.interview.id = :interviewId AND ip.isRequired = true AND ip.confirmedAttendance = false")
    boolean existsRequiredUnconfirmedParticipants(@Param("interviewId") Long interviewId);

    // Get participant count by interview
    @Query("SELECT COUNT(ip) FROM InterviewParticipant ip WHERE ip.interview.id = :interviewId")
    Long countParticipantsByInterviewId(@Param("interviewId") Long interviewId);

    // Find interviews where participant is involved
    @Query("SELECT DISTINCT ip.interview.id FROM InterviewParticipant ip WHERE ip.participantId = :participantId")
    List<Long> findInterviewIdsByParticipantId(@Param("participantId") Long participantId);
}
