package com.itsutra.project.dao;

import com.itsutra.project.entity.MetricValue;
import com.itsutra.project.enums.MetricCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MetricValueDAO extends JpaRepository<MetricValue, Long> {

    List<MetricValue> findByMetricId(Long metricId);
    List<MetricValue> findByMetricIdAndCalculatedAtBetween(Long metricId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT mv FROM MetricValue mv WHERE mv.metric.id = :metricId ORDER BY mv.calculatedAt DESC LIMIT 1")
    Optional<MetricValue> findLatestByMetricId(@Param("metricId") Long metricId);

    @Query("SELECT mv FROM MetricValue mv WHERE mv.metric.code = :metricCode AND mv.calculatedAt BETWEEN :startDate AND :endDate ORDER BY mv.calculatedAt")
    List<MetricValue> findByMetricCodeAndDateRange(@Param("metricCode") String metricCode, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT mv.timePeriod, AVG(mv.value) FROM MetricValue mv WHERE mv.metric.id = :metricId GROUP BY mv.timePeriod")
    List<Object[]> findAverageValuesByTimePeriod(@Param("metricId") Long metricId);

    @Query("SELECT mv FROM MetricValue mv WHERE mv.metric.category = :category AND mv.calculatedAt = (SELECT MAX(mv2.calculatedAt) FROM MetricValue mv2 WHERE mv2.metric.category = :category)")
    List<MetricValue> findLatestValuesByCategory(@Param("category") MetricCategory category);
}
