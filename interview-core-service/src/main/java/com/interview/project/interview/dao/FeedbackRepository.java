package com.interview.project.interview.dao;//package com.itsutra.project.interview.dao;
//
//
//import com.itsutra.project.interview.entity.Feedback;
//import org.springframework.data.r2dbc.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.data.repository.reactive.ReactiveCrudRepository;
//import org.springframework.stereotype.Repository;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//
//@Repository
//public interface FeedbackRepository extends ReactiveCrudRepository<Feedback, Long> {
//
//    Flux<Feedback> findByInterviewId(Long interviewId);
//    Flux<Feedback> findByProvidedBy(Long providedBy);
//    Flux<Feedback> findByProvidedFor(Long providedFor);
//
//    Flux<Feedback> findByInterviewIdAndIsFinalFeedbackTrue(Long interviewId);
//
//    @Query("SELECT f FROM Feedback f WHERE f.interview.id = :interviewId AND f.providedBy = :providedBy")
//    Mono<Feedback> findByInterviewAndProvider(@Param("interviewId") Long interviewId,
//                                              @Param("providedBy") Long providedBy);
//
//    Mono<Feedback> findByInterviewIdAndProvidedBy(Long interviewId, Long providedBy);
//
//    @Query("SELECT AVG(f.overallRating) FROM Feedback f WHERE f.providedFor = :candidateId")
//    Mono<Double> findAverageRatingByCandidate(@Param("candidateId") Long candidateId);
//
//    @Query("SELECT f FROM Feedback f WHERE f.interview.id = :interviewId AND f.isSharedWithCandidate = true")
//    Flux<Feedback> findSharedFeedbacksByInterview(@Param("interviewId") Long interviewId);
//}
