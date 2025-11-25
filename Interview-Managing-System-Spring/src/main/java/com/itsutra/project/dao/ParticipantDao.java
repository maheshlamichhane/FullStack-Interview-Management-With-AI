package com.itsutra.project.dao;

import com.itsutra.project.entity.InterviewParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantDao extends JpaRepository<InterviewParticipant,Long> {
}
