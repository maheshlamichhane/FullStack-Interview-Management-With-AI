package com.itsutra.ai.project.dao;

import com.itsutra.ai.project.entity.ResumeAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResumeAnalysisRepository extends JpaRepository<ResumeAnalysis, UUID> {

    Optional<ResumeAnalysis> findByCandidateId(UUID candidateId);

    Optional<ResumeAnalysis> findByResumeFileId(UUID resumeFileId);

    List<ResumeAnalysis> findByCandidateIdIn(List<UUID> candidateIds);
}
