package com.itsutra.project.dao;


import com.itsutra.project.entity.JobPosition;
import com.itsutra.project.enums.EmploymentType;
import com.itsutra.project.enums.ExperienceLevel;
import com.itsutra.project.enums.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobPositionDAO extends JpaRepository<JobPosition, Long>, JpaSpecificationExecutor<JobPosition> {

    Optional<JobPosition> findByCode(String code);
    Boolean existsByCode(String code);

    Page<JobPosition> findByStatus(JobStatus status, Pageable pageable);
    Page<JobPosition> findByDepartmentId(Long departmentId, Pageable pageable);
    Page<JobPosition> findByLocationId(Long locationId, Pageable pageable);

    @Query("SELECT jp FROM JobPosition jp WHERE jp.status = 'PUBLISHED' AND " +
            "(jp.applicationDeadline IS NULL OR jp.applicationDeadline > :currentDate)")
    Page<JobPosition> findActivePositions(@Param("currentDate") LocalDateTime currentDate, Pageable pageable);

    @Query("SELECT jp FROM JobPosition jp WHERE jp.department.id = :departmentId AND jp.status = 'PUBLISHED'")
    Page<JobPosition> findActivePositionsByDepartment(@Param("departmentId") Long departmentId, Pageable pageable);

    @Query("SELECT jp FROM JobPosition jp WHERE jp.employmentType = :employmentType AND jp.status = 'PUBLISHED'")
    Page<JobPosition> findByEmploymentType(@Param("employmentType") EmploymentType employmentType, Pageable pageable);

    @Query("SELECT jp FROM JobPosition jp WHERE jp.experienceLevel = :experienceLevel AND jp.status = 'PUBLISHED'")
    Page<JobPosition> findByExperienceLevel(@Param("experienceLevel") ExperienceLevel experienceLevel, Pageable pageable);

    @Query("SELECT jp FROM JobPosition jp WHERE jp.isRemote = true AND jp.status = 'PUBLISHED'")
    Page<JobPosition> findRemotePositions(Pageable pageable);

    @Query("SELECT jp FROM JobPosition jp WHERE jp.minSalary >= :minSalary AND jp.status = 'PUBLISHED'")
    Page<JobPosition> findByMinSalary(@Param("minSalary") Double minSalary, Pageable pageable);

    @Query("SELECT COUNT(jp) FROM JobPosition jp WHERE jp.status = 'PUBLISHED'")
    Long countActivePositions();

    @Query("SELECT jp FROM JobPosition jp WHERE jp.status = 'PUBLISHED' AND jp.applicationDeadline < :currentDate")
    List<JobPosition> findExpiredPositions(@Param("currentDate") LocalDateTime currentDate);

    @Query("SELECT jp FROM JobPosition jp JOIN jp.requiredSkills js WHERE js.skillName = :skillName AND jp.status = 'PUBLISHED'")
    Page<JobPosition> findByRequiredSkill(@Param("skillName") String skillName, Pageable pageable);
}
