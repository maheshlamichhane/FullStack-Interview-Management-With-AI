//package com.itsutra.ai.project.dao;
//
//import com.itsutra.ai.project.entity.GeneratedQuestion;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.UUID;
//
//
//@Repository
//public interface GeneratedQuestionRepository extends JpaRepository<GeneratedQuestion, UUID> {
//
//    List<GeneratedQuestion> findByJobId(UUID jobId);
//
//    List<GeneratedQuestion> findByCategory(String category);
//
//    List<GeneratedQuestion> findByDifficulty(String difficulty);
//
//    List<GeneratedQuestion> findByJobIdAndDifficulty(UUID jobId, String difficulty);
//
////    @Query("SELECT gq FROM GeneratedQuestion gq WHERE gq.tags @> ARRAY[:tag]")
////    List<GeneratedQuestion> findByTag(@Param("tag") String tag);
//}
