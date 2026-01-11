//package com.itsutra.project.candidate.dao;
//
//
//import com.itsutra.project.candidate.entity.Candidate;
//import com.itsutra.project.candidate.enums.EmploymentStatus;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//public interface CandidateDAO extends JpaRepository<Candidate, Long>, JpaSpecificationExecutor<Candidate> {
//
//    Optional<Candidate> findByEmail(String email);
//    Boolean existsByEmail(String email);
//
//    Page<Candidate> findByIsActive(Boolean isActive, Pageable pageable);
//
//    @Query("SELECT c FROM Candidate c WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
//    Page<Candidate> findByNameContaining(@Param("name") String name, Pageable pageable);
//
//    @Query("SELECT c FROM Candidate c WHERE c.currentLocation = :location")
//    Page<Candidate> findByLocation(@Param("location") String location, Pageable pageable);
//
//    @Query("SELECT c FROM Candidate c WHERE c.totalExperience BETWEEN :minExp AND :maxExp")
//    Page<Candidate> findByExperienceRange(@Param("minExp") Double minExp, @Param("maxExp") Double maxExp, Pageable pageable);
//
//    @Query("SELECT c FROM Candidate c WHERE c.noticePeriod <= :maxNoticePeriod")
//    Page<Candidate> findByNoticePeriodLessThanEqual(@Param("maxNoticePeriod") Integer maxNoticePeriod, Pageable pageable);
//
//    @Query("SELECT c FROM Candidate c JOIN c.skills s WHERE s.skillName = :skillName")
//    Page<Candidate> findBySkillName(@Param("skillName") String skillName, Pageable pageable);
//
//    @Query("SELECT c FROM Candidate c WHERE c.employmentStatus = :status")
//    Page<Candidate> findByEmploymentStatus(@Param("status") EmploymentStatus status, Pageable pageable);
//}
