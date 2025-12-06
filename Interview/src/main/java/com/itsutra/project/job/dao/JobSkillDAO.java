package com.itsutra.project.job.dao;

import com.itsutra.project.job.entity.JobSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface JobSkillDAO extends JpaRepository<JobSkill, Long> {

    List<JobSkill> findByJobPositionId(Long jobPositionId);
    List<JobSkill> findBySkillName(String skillName);
    List<JobSkill> findBySkillNameIn(Set<String> skillNames);

    @Query("SELECT DISTINCT js.skillName FROM JobSkill js")
    List<String> findDistinctSkillNames();

    @Query("SELECT js FROM JobSkill js WHERE js.jobPosition.id = :jobPositionId AND js.isMandatory = true")
    List<JobSkill> findMandatorySkillsByJobPosition(@Param("jobPositionId") Long jobPositionId);

    @Query("SELECT js.skillName, COUNT(js) FROM JobSkill js GROUP BY js.skillName ORDER BY COUNT(js) DESC")
    List<Object[]> findPopularSkills();

    void deleteByJobPositionId(Long jobPositionId);
}
