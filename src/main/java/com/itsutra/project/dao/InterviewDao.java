package com.itsutra.project.dao;

import com.itsutra.project.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InterviewDao extends JpaRepository<Interview,Long> {

    @Query("SELECT DISTINCT i FROM Interview i LEFT JOIN FETCH i.participants")
    List<Interview> findAllWithParticipants();

    @Query("SELECT DISTINCT i FROM Interview i LEFT JOIN FETCH i.participants WHERE i.id = :interviewId")
    Interview findByIdWithParticipants(@Param("interviewId") Long interviewId);

}
