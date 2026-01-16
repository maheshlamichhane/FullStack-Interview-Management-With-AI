package com.core.project.candidate.dao;//package com.itsutra.project.candidate.dao;
//
//
//import com.itsutra.project.candidate.entity.CandidateSkill;
//import com.itsutra.project.candidate.enums.ProficiencyLevel;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//
//@Repository
//public interface CandidateSkillDAO extends JpaRepository<CandidateSkill, Long> {
//
//    List<CandidateSkill> findByCandidateId(Long candidateId);
//    List<CandidateSkill> findBySkillNameIn(Set<String> skillNames);
//
//    @Query("SELECT cs FROM CandidateSkill cs WHERE cs.candidate.id = :candidateId AND cs.skillName = :skillName")
//    Optional<CandidateSkill> findByCandidateIdAndSkillName(@Param("candidateId") Long candidateId, @Param("skillName") String skillName);
//
//    @Query("SELECT DISTINCT cs.skillName FROM CandidateSkill cs")
//    List<String> findAllDistinctSkillNames();
//
//    @Query("SELECT cs FROM CandidateSkill cs WHERE cs.candidate.id = :candidateId AND cs.proficiencyLevel = :proficiencyLevel")
//    List<CandidateSkill> findByCandidateIdAndProficiencyLevel(@Param("candidateId") Long candidateId, @Param("proficiencyLevel") ProficiencyLevel proficiencyLevel);
//
//    void deleteByCandidateId(Long candidateId);
//}
