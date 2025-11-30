package com.itsutra.project.dao;

import com.itsutra.project.entity.AIResponse;
import com.itsutra.project.enums.ResponseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AIResponseDao extends JpaRepository<AIResponse, Long> {
    Optional<AIResponse> findByRequestId(Long requestId);
    List<AIResponse> findByStatus(ResponseStatus status);
}
