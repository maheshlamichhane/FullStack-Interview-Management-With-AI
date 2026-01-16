package com.core.project.report.service;//package com.itsutra.project.report.service;
//
//
//import com.itsutra.project.report.entity.Metric;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class DataCalculationService {
//
////    private final MetricDataService metricDataService;
////    private final InterviewService interviewService;
////    private final CandidateService candidateService;
////    private final JobPositionService jobPositionService;
//
//    /**
//     * Calculate metric value based on metric configuration and data sources
//     */
//    public Double calculateMetric(Metric metric) {
//        log.debug("Calculating metric: {} with formula: {}", metric.getName(), metric.getCalculationFormula());
//
//        try {
//            // If metric has a custom calculation formula, use it
//            if (metric.getCalculationFormula() != null && !metric.getCalculationFormula().trim().isEmpty()) {
////                return calculateCustomMetric(metric);
//                return null;
//            }
//
//            // Otherwise, use predefined calculations based on metric code
////            return calculatePredefinedMetric(metric);
//            return null;
//
//        } catch (Exception e) {
//            log.error("Error calculating metric: {}", metric.getName(), e);
//            return null;
//        }
//    }
//
//    /**
//     * Calculate custom metrics using formula engine
//     */
////    private Double calculateCustomMetric(Metric metric) {
////        // This would integrate with a formula engine like Exp4j, MVEL, or custom parser
////        // For now, implementing common recruitment metrics
////
////        String formula = metric.getCalculationFormula().toUpperCase();
////
////        // Simple formula parser for common operations
////        if (formula.contains("TIME_TO_HIRE")) {
////            return calculateTimeToHire(metric);
////        } else if (formula.contains("INTERVIEW_SUCCESS_RATE")) {
////            return calculateInterviewSuccessRate(metric);
////        } else if (formula.contains("OFFER_ACCEPTANCE_RATE")) {
////            return calculateOfferAcceptanceRate(metric);
////        } else if (formula.contains("COST_PER_HIRE")) {
////            return calculateCostPerHire(metric);
////        } else if (formula.contains("QUALITY_OF_HIRE")) {
////            return calculateQualityOfHire(metric);
////        } else if (formula.contains("DIVERSITY_RATIO")) {
////            return calculateDiversityRatio(metric);
////        }
////
////        log.warn("Unknown formula for metric: {}", metric.getName());
////        return 0.0;
////    }
////
////    /**
////     * Calculate predefined metrics based on metric code
////     */
////    private Double calculatePredefinedMetric(Metric metric) {
////        return switch (metric.getCode()) {
////            case "TIME_TO_HIRE" -> calculateTimeToHire(metric);
////            case "TIME_TO_FILL" -> calculateTimeToFill(metric);
////            case "INTERVIEW_SUCCESS_RATE" -> calculateInterviewSuccessRate(metric);
////            case "OFFER_ACCEPTANCE_RATE" -> calculateOfferAcceptanceRate(metric);
////            case "APPLICATION_COMPLETION_RATE" -> calculateApplicationCompletionRate(metric);
////            case "COST_PER_HIRE" -> calculateCostPerHire(metric);
////            case "QUALITY_OF_HIRE" -> calculateQualityOfHire(metric);
////            case "SOURCE_EFFECTIVENESS" -> calculateSourceEffectiveness(metric);
////            case "DIVERSITY_RATIO" -> calculateDiversityRatio(metric);
////            case "CANDIDATE_SATISFACTION" -> calculateCandidateSatisfaction(metric);
////            case "HIRING_MANAGER_SATISFACTION" -> calculateHiringManagerSatisfaction(metric);
////            case "OPEN_POSITIONS" -> calculateOpenPositions(metric);
////            case "FILLED_POSITIONS" -> calculateFilledPositions(metric);
////            default -> {
////                log.warn("No calculation defined for metric code: {}", metric.getCode());
////                yield 0.0;
////            }
////        };
////    }
////
////    // Specific Metric Calculation Methods
////
////    /**
////     * Calculate average time from application to hire
////     */
////    private Double calculateTimeToHire(Metric metric) {
////        Map<String, Object> filters = extractFilters(metric);
////        LocalDateTime startDate = getStartDate(filters);
////        LocalDateTime endDate = getEndDate(filters);
////
////        // Get hired candidates within date range
////        Long timeToHireSum = candidateService.getTimeToHireSum(startDate, endDate, filters);
////        Long hiredCount = candidateService.getHiredCount(startDate, endDate, filters);
////
////        if (hiredCount == 0) return 0.0;
////        return timeToHireSum / (double) hiredCount; // Return in days
////    }
////
////    /**
////     * Calculate average time from job posting to hire
////     */
////    private Double calculateTimeToFill(Metric metric) {
////        Map<String, Object> filters = extractFilters(metric);
////        LocalDateTime startDate = getStartDate(filters);
////        LocalDateTime endDate = getEndDate(filters);
////
////        Long timeToFillSum = jobPositionService.getTimeToFillSum(startDate, endDate, filters);
////        Long filledCount = jobPositionService.getFilledPositionsCount(startDate, endDate, filters);
////
////        if (filledCount == 0) return 0.0;
////        return timeToFillSum / (double) filledCount; // Return in days
////    }
////
////    /**
////     * Calculate interview success rate (candidates moving to next stage)
////     */
////    private Double calculateInterviewSuccessRate(Metric metric) {
////        Map<String, Object> filters = extractFilters(metric);
////        LocalDateTime startDate = getStartDate(filters);
////        LocalDateTime endDate = getEndDate(filters);
////
////        Long successfulInterviews = interviewService.getSuccessfulInterviewsCount(startDate, endDate, filters);
////        Long totalInterviews = interviewService.getTotalInterviewsCount(startDate, endDate, filters);
////
////        if (totalInterviews == 0) return 0.0;
////        return (successfulInterviews / (double) totalInterviews) * 100; // Return as percentage
////    }
////
////    /**
////     * Calculate offer acceptance rate
////     */
////    private Double calculateOfferAcceptanceRate(Metric metric) {
////        Map<String, Object> filters = extractFilters(metric);
////        LocalDateTime startDate = getStartDate(filters);
////        LocalDateTime endDate = getEndDate(filters);
////
////        Long acceptedOffers = candidateService.getAcceptedOffersCount(startDate, endDate, filters);
////        Long totalOffers = candidateService.getTotalOffersCount(startDate, endDate, filters);
////
////        if (totalOffers == 0) return 0.0;
////        return (acceptedOffers / (double) totalOffers) * 100; // Return as percentage
////    }
////
////    /**
////     * Calculate application completion rate
////     */
////    private Double calculateApplicationCompletionRate(Metric metric) {
////        Map<String, Object> filters = extractFilters(metric);
////        LocalDateTime startDate = getStartDate(filters);
////        LocalDateTime endDate = getEndDate(filters);
////
////        Long completedApplications = candidateService.getCompletedApplicationsCount(startDate, endDate, filters);
////        Long startedApplications = candidateService.getStartedApplicationsCount(startDate, endDate, filters);
////
////        if (startedApplications == 0) return 0.0;
////        return (completedApplications / (double) startedApplications) * 100; // Return as percentage
////    }
////
////    /**
////     * Calculate cost per hire
////     */
////    private Double calculateCostPerHire(Metric metric) {
////        Map<String, Object> filters = extractFilters(metric);
////        LocalDateTime startDate = getStartDate(filters);
////        LocalDateTime endDate = getEndDate(filters);
////
////        Double totalCost = candidateService.getTotalHiringCost(startDate, endDate, filters);
////        Long hiredCount = candidateService.getHiredCount(startDate, endDate, filters);
////
////        if (hiredCount == 0) return 0.0;
////        return totalCost / hiredCount; // Return cost per hire
////    }
////
////    /**
////     * Calculate quality of hire (based on performance ratings)
////     */
////    private Double calculateQualityOfHire(Metric metric) {
////        Map<String, Object> filters = extractFilters(metric);
////        LocalDateTime startDate = getStartDate(filters);
////        LocalDateTime endDate = getEndDate(filters);
////
////        Double totalPerformanceScore = candidateService.getTotalPerformanceScore(startDate, endDate, filters);
////        Long ratedHiresCount = candidateService.getRatedHiresCount(startDate, endDate, filters);
////
////        if (ratedHiresCount == 0) return 0.0;
////        return totalPerformanceScore / ratedHiresCount; // Return average performance score (1-5 scale)
////    }
////
////    /**
////     * Calculate source effectiveness (hires per source)
////     */
////    private Double calculateSourceEffectiveness(Metric metric) {
////        Map<String, Object> filters = extractFilters(metric);
////        String source = (String) filters.get("source");
////
////        if (source == null) {
////            // Calculate overall source effectiveness index
////            return calculateOverallSourceEffectiveness(metric);
////        }
////
////        // Calculate effectiveness for specific source
////        LocalDateTime startDate = getStartDate(filters);
////        LocalDateTime endDate = getEndDate(filters);
////
////        Long hiresFromSource = candidateService.getHiresFromSource(source, startDate, endDate, filters);
////        Long applicationsFromSource = candidateService.getApplicationsFromSource(source, startDate, endDate, filters);
////
////        if (applicationsFromSource == 0) return 0.0;
////        return (hiresFromSource / (double) applicationsFromSource) * 100; // Return conversion rate
////    }
////
////    /**
////     * Calculate overall source effectiveness index
////     */
////    private Double calculateOverallSourceEffectiveness(Metric metric) {
////        // This would calculate a composite score based on multiple factors:
////        // - Conversion rate
////        // - Time to hire from source
////        // - Quality of hires from source
////        // - Cost per hire from source
////
////        Map<String, Object> filters = extractFilters(metric);
////        LocalDateTime startDate = getStartDate(filters);
////        LocalDateTime endDate = getEndDate(filters);
////
////        // Simplified calculation - average conversion rate across all sources
////        Map<String, Double> sourceConversionRates = candidateService.getSourceConversionRates(startDate, endDate, filters);
////
////        if (sourceConversionRates.isEmpty()) return 0.0;
////
////        double totalRate = sourceConversionRates.values().stream().mapToDouble(Double::doubleValue).sum();
////        return totalRate / sourceConversionRates.size();
////    }
////
////    /**
////     * Calculate diversity ratio
////     */
////    private Double calculateDiversityRatio(Metric metric) {
////        Map<String, Object> filters = extractFilters(metric);
////        LocalDateTime startDate = getStartDate(filters);
////        LocalDateTime endDate = getEndDate(filters);
////        String diversityDimension = (String) filters.getOrDefault("dimension", "gender");
////
////        Map<String, Long> diversityStats = candidateService.getDiversityStats(diversityDimension, startDate, endDate, filters);
////
////        if (diversityStats.isEmpty() || diversityStats.size() < 2) return 0.0;
////
////        // Calculate diversity index (1 - Herfindahl index)
////        Long total = diversityStats.values().stream().mapToLong(Long::longValue).sum();
////        if (total == 0) return 0.0;
////
////        double herfindahlIndex = diversityStats.values().stream()
////                .mapToDouble(count -> Math.pow(count / (double) total, 2))
////                .sum();
////
////        return (1 - herfindahlIndex) * 100; // Return as percentage
////    }
////
////    /**
////     * Calculate candidate satisfaction score
////     */
////    private Double calculateCandidateSatisfaction(Metric metric) {
////        Map<String, Object> filters = extractFilters(metric);
////        LocalDateTime startDate = getStartDate(filters);
////        LocalDateTime endDate = getEndDate(filters);
////
////        Double totalSatisfactionScore = candidateService.getTotalSatisfactionScore(startDate, endDate, filters);
////        Long surveyedCandidatesCount = candidateService.getSurveyedCandidatesCount(startDate, endDate, filters);
////
////        if (surveyedCandidatesCount == 0) return 0.0;
////        return totalSatisfactionScore / surveyedCandidatesCount; // Return average satisfaction (1-5 scale)
////    }
////
////    /**
////     * Calculate hiring manager satisfaction score
////     */
////    private Double calculateHiringManagerSatisfaction(Metric metric) {
////        Map<String, Object> filters = extractFilters(metric);
////        LocalDateTime startDate = getStartDate(filters);
////        LocalDateTime endDate = getEndDate(filters);
////
////        Double totalSatisfactionScore = candidateService.getHiringManagerSatisfactionScore(startDate, endDate, filters);
////        Long surveyCount = candidateService.getHiringManagerSurveyCount(startDate, endDate, filters);
////
////        if (surveyCount == 0) return 0.0;
////        return totalSatisfactionScore / surveyCount; // Return average satisfaction (1-5 scale)
////    }
////
////    /**
////     * Calculate number of open positions
////     */
////    private Double calculateOpenPositions(Metric metric) {
////        Map<String, Object> filters = extractFilters(metric);
////        return (double) jobPositionService.getOpenPositionsCount(filters);
////    }
////
////    /**
////     * Calculate number of filled positions in period
////     */
////    private Double calculateFilledPositions(Metric metric) {
////        Map<String, Object> filters = extractFilters(metric);
////        LocalDateTime startDate = getStartDate(filters);
////        LocalDateTime endDate = getEndDate(filters);
////
////        return (double) jobPositionService.getFilledPositionsCount(startDate, endDate, filters);
////    }
////
////    // Helper methods
////
////    private Map<String, Object> extractFilters(Metric metric) {
////        // Extract filters from metric configuration or use defaults
////        // This would parse the metric's data source or configuration for filters
////        return new HashMap<>();
////    }
////
////    private LocalDateTime getStartDate(Map<String, Object> filters) {
////        Object startDate = filters.get("startDate");
////        if (startDate instanceof LocalDateTime) {
////            return (LocalDateTime) startDate;
////        }
////        // Default to 30 days ago
////        return LocalDateTime.now().minusDays(30);
////    }
////
////    private LocalDateTime getEndDate(Map<String, Object> filters) {
////        Object endDate = filters.get("endDate");
////        if (endDate instanceof LocalDateTime) {
////            return (LocalDateTime) endDate;
////        }
////        // Default to now
////        return LocalDateTime.now();
////    }
////
////    /**
////     * Calculate trend for a metric based on historical data
////     */
////    public Double calculateTrend(Metric metric, int periods) {
////        // This would calculate the trend (slope) of the metric over the specified number of periods
////        // Using linear regression or simple difference calculation
////
////        // Implementation would fetch historical values and calculate trend
////        return 0.0; // Placeholder
////    }
////
////    /**
////     * Forecast future metric values
////     */
////    public Double forecastMetric(Metric metric, int periodsAhead) {
////        // This would use time series forecasting (ARIMA, exponential smoothing, etc.)
////        // to predict future values of the metric
////
////        // Implementation would depend on the forecasting algorithm
////        return 0.0; // Placeholder
////    }
//}
