package com.itsutra.project.report.dao;

import com.itsutra.project.report.entity.Metric;
import com.itsutra.project.report.enums.MetricCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MetricDAO extends JpaRepository<Metric, Long> {

    Optional<Metric> findByCode(String code);
    Optional<Metric> findByIdAndCreatedById(Long id, Long createdById);
    List<Metric> findByCreatedById(Long createdById);
    Boolean existsByCodeAndCreatedById(String code,Long userId);

    Page<Metric> findByCategory(MetricCategory category, Pageable pageable);
    Page<Metric> findByIsActive(Boolean isActive, Pageable pageable);

    @Query("SELECT m FROM Metric m WHERE m.isActive = true AND m.isTrendAvailable = true")
    List<Metric> findMetricsWithTrends();

    @Query("SELECT m FROM Metric m WHERE m.category IN :categories AND m.isActive = true")
    List<Metric> findMetricsByCategories(@Param("categories") List<MetricCategory> categories);

    @Query("SELECT m.category, COUNT(m) FROM Metric m WHERE m.isActive = true GROUP BY m.category")
    List<Object[]> countMetricsByCategory();

    @Query("SELECT mv.metric, mv FROM MetricValue mv WHERE mv.metric.id = :metricId AND mv.calculatedAt BETWEEN :startDate AND :endDate ORDER BY mv.calculatedAt")
    List<Object[]> findMetricValuesWithMetrics(@Param("metricId") Long metricId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
