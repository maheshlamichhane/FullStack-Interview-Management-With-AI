package com.itsutra.project.dao;


import com.itsutra.project.entity.ReportExecution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportExecutionDAO extends JpaRepository<ReportExecution, Long> {

    long countByReportId(Long reportId);

    Page<ReportExecution> findByReportId(Long reportId, Pageable pageable);
    List<ReportExecution> findByStatus(ReportExecution.ExecutionStatus status);

    @Query("SELECT re FROM ReportExecution re WHERE re.report.id = :reportId AND re.executedAt BETWEEN :startDate AND :endDate")
    List<ReportExecution> findExecutionsByReportAndDateRange(@Param("reportId") Long reportId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT AVG(re.executionTimeMs) FROM ReportExecution re WHERE re.report.id = :reportId AND re.status = 'COMPLETED'")
    Double findAverageExecutionTimeByReport(@Param("reportId") Long reportId);

    @Query("SELECT re.status, COUNT(re) FROM ReportExecution re WHERE re.report.id = :reportId GROUP BY re.status")
    List<Object[]> countExecutionsByStatus(@Param("reportId") Long reportId);

    @Query("SELECT re FROM ReportExecution re WHERE re.status = 'RUNNING' AND re.executedAt < :cutoffTime")
    List<ReportExecution> findLongRunningExecutions(@Param("cutoffTime") LocalDateTime cutoffTime);
}
