package com.core.project.candidate.dao;//package com.itsutra.project.candidate.dao;
//
//
//import com.itsutra.project.candidate.entity.Resume;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface ResumeDAO extends JpaRepository<Resume, Long> {
//
//    List<Resume> findByCandidateId(Long candidateId);
//    Optional<Resume> findByCandidateIdAndIsPrimary(Long candidateId, Boolean isPrimary);
//
//    @Query("SELECT r FROM Resume r WHERE r.candidate.id = :candidateId ORDER BY r.uploadedAt DESC")
//    List<Resume> findLatestByCandidateId(@Param("candidateId") Long candidateId);
//
//    @Query("SELECT r FROM Resume r WHERE r.parsingStatus = 'PENDING'")
//    List<Resume> findPendingParsingResumes();
//
//    Long countByCandidateId(Long candidateId);
//}