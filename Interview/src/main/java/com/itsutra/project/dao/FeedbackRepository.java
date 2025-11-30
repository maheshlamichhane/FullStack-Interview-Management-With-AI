package com.itsutra.project.dao;


import com.itsutra.project.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findByInterviewId(Long interviewId);
    List<Feedback> findByProvidedBy(Long providedBy);
    List<Feedback> findByProvidedFor(Long providedFor);

    List<Feedback> findByInterviewIdAndIsFinalFeedbackTrue(Long interviewId);

    @Query("SELECT f FROM Feedback f WHERE f.interview.id = :interviewId AND f.providedBy = :providedBy")
    Optional<Feedback> findByInterviewAndProvider(@Param("interviewId") Long interviewId,
                                                  @Param("providedBy") Long providedBy);

    Optional<Feedback> findByInterviewIdAndProvidedBy(Long interviewId, Long providedBy);

    @Query("SELECT AVG(f.overallRating) FROM Feedback f WHERE f.providedFor = :candidateId")
    Double findAverageRatingByCandidate(@Param("candidateId") Long candidateId);

    @Query("SELECT f FROM Feedback f WHERE f.interview.id = :interviewId AND f.isSharedWithCandidate = true")
    List<Feedback> findSharedFeedbacksByInterview(@Param("interviewId") Long interviewId);
}
