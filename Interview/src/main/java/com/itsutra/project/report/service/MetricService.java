package com.itsutra.project.report.service;

import com.itsutra.project.report.dao.MetricDAO;
import com.itsutra.project.report.dao.MetricValueDAO;
import com.itsutra.project.report.dto.*;
import com.itsutra.project.report.entity.Metric;
import com.itsutra.project.report.entity.MetricValue;
import com.itsutra.project.report.entity.User;
import com.itsutra.project.report.mapper.AnalyticsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MetricService {

    private final MetricDAO metricDAO;
    private final MetricValueDAO metricValueDAO;
    private final AnalyticsMapper analyticsMapper;
    private final DataCalculationService dataCalculationService;
    private final AuthenticationService authenticationService;

    @Transactional
    public MetricResponseDTO createMetric(MetricRequestDTO request) {
        log.info("Creating new metric with code: {}", request.getCode());

        User user = authenticationService.getCurrentUser();

        if (metricDAO.existsByCodeAndCreatedById(request.getCode(),user.getId())) {
            throw new IllegalArgumentException("Metric code already exists: " + request.getCode());
        }

        Metric metric = analyticsMapper.toMetricEntity(request);
        metric.setCreatedBy(user);
        Metric savedMetric = metricDAO.save(metric);

        log.info("Successfully created metric with id: {}", savedMetric.getId());
        return analyticsMapper.toMetricResponse(savedMetric, null, null);
    }



    @Transactional(readOnly = true)
    public MetricResponseDTO getMetricById(Long id) {
        log.debug("Fetching metric by id: {}", id);

        User user = authenticationService.getCurrentUser();

        Metric metric = metricDAO.findByIdAndCreatedById(id,user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Metric not found with id: " + id));

        MetricValue currentValue = metricValueDAO.findLatestByMetricId(id).orElse(null);
        List<MetricValue> historicalValues = metricValueDAO.findByMetricIdAndCalculatedAtBetween(
                id, LocalDateTime.now().minusDays(30), LocalDateTime.now());

        return analyticsMapper.toMetricResponse(metric, currentValue, historicalValues);
    }





    @Transactional(readOnly = true)
    public List<MetricResponseDTO> getAllMetrics() {
        log.debug("Fetching all metrics");
        User user = authenticationService.getCurrentUser();
        return metricDAO.findByCreatedById(user.getId()).stream()
                .map(metric -> {
                    MetricValue currentValue = metricValueDAO.findLatestByMetricId(metric.getId()).orElse(null);
                    return analyticsMapper.toMetricResponse(metric, currentValue, null);
                }).toList();
    }



    @Transactional
    public MetricResponseDTO updateMetric(Long id, MetricRequestDTO request) {
        log.info("Updating metric with id: {}", id);

        User user = authenticationService.getCurrentUser();
        Metric metric = metricDAO.findByIdAndCreatedById(id,user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Metric not found with id: " + id));


        // Validate code uniqueness if changed
        if (request.getCode() != null && !request.getCode().equals(metric.getCode())) {
            if (metricDAO.existsByCodeAndCreatedById(request.getCode(),user.getId())) {
                throw new IllegalArgumentException("Metric code already exists: " + request.getCode());
            }
            metric.setCode(request.getCode());
        }

        // Update fields
        Optional.ofNullable(request.getName()).ifPresent(metric::setName);
        Optional.ofNullable(request.getDescription()).ifPresent(metric::setDescription);
        Optional.ofNullable(request.getCategory()).ifPresent(metric::setCategory);
        Optional.ofNullable(request.getDataType()).ifPresent(metric::setDataType);
        Optional.ofNullable(request.getCalculationFormula()).ifPresent(metric::setCalculationFormula);
        Optional.ofNullable(request.getDataSource()).ifPresent(metric::setDataSource);
        Optional.ofNullable(request.getAggregationType()).ifPresent(metric::setAggregationType);
        Optional.ofNullable(request.getUnit()).ifPresent(metric::setUnit);
        Optional.ofNullable(request.getTargetValue()).ifPresent(metric::setTargetValue);
        Optional.ofNullable(request.getWarningThreshold()).ifPresent(metric::setWarningThreshold);
        Optional.ofNullable(request.getCriticalThreshold()).ifPresent(metric::setCriticalThreshold);
        Optional.ofNullable(request.getIsTrendAvailable()).ifPresent(metric::setIsTrendAvailable);

        Metric updatedMetric = metricDAO.save(metric);
        MetricValue currentValue = metricValueDAO.findLatestByMetricId(id).orElse(null);

        log.info("Successfully updated metric with id: {}", id);
        return analyticsMapper.toMetricResponse(updatedMetric, currentValue, null);
    }


    @Transactional
    public void deleteMetric(Long id) {
        log.info("Deleting metric with id: {}", id);
        User user = authenticationService.getCurrentUser();
        Metric metric = metricDAO.findByIdAndCreatedById(id,user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Metric not found with id: " + id));
        metricDAO.delete(metric);
        log.info("Successfully deleted metric with id: {}", id);
    }


    @Transactional
    public MetricValueResponseDTO addMetricValue(MetricValueRequestDTO request) {
        log.info("Adding value for metric id: {}", request.getMetricId());

        User user = authenticationService.getCurrentUser();
        Metric metric = metricDAO.findByIdAndCreatedById(request.getMetricId(),user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Metric not found with id: " + request.getMetricId()));

        MetricValue metricValue = MetricValue.builder()
                .metric(metric)
                .value(request.getValue())
                .calculatedAt(request.getCalculatedAt() != null ? request.getCalculatedAt() : LocalDateTime.now())
                .timePeriod(request.getTimePeriod())
                .dimensionFilters(analyticsMapper.convertToJson(request.getDimensionFilters()))
                .build();

        // Calculate change percentage if previous value exists
        Optional<MetricValue> previousValue = metricValueDAO.findLatestByMetricId(request.getMetricId());
        if (previousValue.isPresent()) {
            metricValue.setPreviousValue(previousValue.get().getValue());
            if (previousValue.get().getValue() != 0) {
                double change = ((request.getValue() - previousValue.get().getValue()) / previousValue.get().getValue()) * 100;
                metricValue.setChangePercentage(change);
            }
        }
        MetricValue savedValue = metricValueDAO.save(metricValue);
        log.info("Successfully added metric value for metric id: {}", request.getMetricId());
        return analyticsMapper.toMetricValueResponse(savedValue);
    }


    @Transactional(readOnly = true)
    public List<MetricValueResponseDTO> getMetricValues(Long metricId, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Fetching metric values for metric id: {} between {} and {}", metricId, startDate, endDate);

        User user = authenticationService.getCurrentUser();
        List<MetricValue> values = metricValueDAO.findByMetricIdAndCalculatedAtBetweenAndMetricCreatedById(metricId,startDate, endDate,user.getId());
        return values.stream()
                .map(analyticsMapper::toMetricValueResponse)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public MetricTrendResponseDTO getMetricTrend(Long metricId, String timeRange) {
        log.debug("Fetching trend for metric id: {} with time range: {}", metricId, timeRange);

        User user = authenticationService.getCurrentUser();
        Metric metric = metricDAO.findByIdAndCreatedById(metricId,user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Metric not found with id: " + metricId));

        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = calculateStartDate(timeRange, endDate);

        List<MetricValue> values = metricValueDAO.findByMetricIdAndCalculatedAtBetweenAndMetricCreatedById(metricId, startDate, endDate,user.getId());
        List<MetricValueResponseDTO> valueResponses = values.stream()
                .map(analyticsMapper::toMetricValueResponse)
                .collect(Collectors.toList());

        MetricTrendResponseDTO response = new MetricTrendResponseDTO();
        response.setMetric(analyticsMapper.toMetricResponse(metric, null, null));
        response.setValues(valueResponses);

        // Calculate statistics
        if (!values.isEmpty()) {
            DoubleSummaryStatistics stats = values.stream()
                    .mapToDouble(MetricValue::getValue)
                    .summaryStatistics();

            response.setAverageValue(stats.getAverage());
            response.setMinValue(stats.getMin());
            response.setMaxValue(stats.getMax());

            // Calculate trend percentage
            if (values.size() > 1) {
                double firstValue = values.get(0).getValue();
                double lastValue = values.get(values.size() - 1).getValue();
                if (firstValue != 0) {
                    double trend = ((lastValue - firstValue) / firstValue) * 100;
                    response.setTrendPercentage(trend);
                }
            }
        }

        return response;
    }







//



//
//    // Metric Calculations
//    @Scheduled(cron = "0 0 * * * *") // Run every hour
//    public void calculateScheduledMetrics() {
//        log.info("Calculating scheduled metrics");
//
//        List<Metric> metrics = metricDAO.findMetricsWithTrends();
//
//        for (Metric metric : metrics) {
//            try {
//                calculateMetricValue(metric);
//            } catch (Exception e) {
//                log.error("Error calculating metric: {}", metric.getName(), e);
//            }
//        }
//    }
//
//    public void calculateMetricValue(Metric metric) {
//        log.debug("Calculating value for metric: {}", metric.getName());
//
//        try {
//            Double value = dataCalculationService.calculateMetric(metric);
//
//            if (value != null) {
//                MetricValueRequestDTO request = new MetricValueRequestDTO();
//                request.setMetricId(metric.getId());
//                request.setValue(value);
//                request.setCalculatedAt(LocalDateTime.now());
//                request.setTimePeriod("HOURLY");
//
//                addMetricValue(request);
//                log.info("Successfully calculated metric: {} with value: {}", metric.getName(), value);
//            }
//        } catch (Exception e) {
//            log.error("Error calculating metric value for: {}", metric.getName(), e);
//        }
//    }
//

//
//    @Transactional(readOnly = true)
//    public Map<String, Object> getMetricsOverview() {
//        log.debug("Fetching metrics overview");
//
//        Map<String, Object> overview = new HashMap<>();
//
//        // Count metrics by category
//        List<Object[]> categoryCounts = metricDAO.countMetricsByCategory();
//        overview.put("metricsByCategory", categoryCounts.stream()
//                .collect(Collectors.toMap(
//                        arr -> arr[0].toString(),
//                        arr -> arr[1]
//                )));
//
//        // Get metrics with trends
//        List<Metric> trendingMetrics = metricDAO.findMetricsWithTrends();
//        overview.put("trendingMetrics", trendingMetrics.stream()
//                .map(metric -> analyticsMapper.toMetricResponse(metric, null, null))
//                .collect(Collectors.toList()));
//
//        return overview;
//    }
//
    // Helper methods
    private LocalDateTime calculateStartDate(String timeRange, LocalDateTime endDate) {
        return switch (timeRange.toUpperCase()) {
            case "1H" -> endDate.minusHours(1);
            case "24H" -> endDate.minusHours(24);
            case "7D" -> endDate.minusDays(7);
            case "30D" -> endDate.minusDays(30);
            case "90D" -> endDate.minusDays(90);
            case "1Y" -> endDate.minusYears(1);
            default -> endDate.minusDays(30);
        };
    }
}