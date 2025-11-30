package com.itsutra.project.service;

import com.itsutra.project.dto.ReportDataResponseDTO;
import com.itsutra.project.entity.Report;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataQueryService {

    private final JdbcTemplate jdbcTemplate;

    public ReportDataResponseDTO executeReportQuery(Report report, Map<String, Object> parameters) {
        log.info("Executing report query for: {}", report.getName());

        try {
            String finalQuery = buildFinalQuery(report.getSqlQuery(), parameters);

            List<Map<String, Object>> data = jdbcTemplate.queryForList(finalQuery);

            ReportDataResponseDTO response = new ReportDataResponseDTO();
            response.setData(data);
            response.setTotalRecords((long) data.size());
            response.setGeneratedAt(java.time.LocalDateTime.now());

            // Calculate summary statistics if needed
            if (!data.isEmpty()) {
                response.setSummary(calculateSummary(data));
            }

            return response;

        } catch (Exception e) {
            log.error("Error executing report query: {}", report.getName(), e);
            throw new RuntimeException("Failed to execute report query: " + e.getMessage(), e);
        }
    }

    private String buildFinalQuery(String baseQuery, Map<String, Object> parameters) {
        String finalQuery = baseQuery;

        if (parameters != null && !parameters.isEmpty()) {
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                String paramName = entry.getKey();
                Object paramValue = entry.getValue();

                if (paramValue instanceof String) {
                    finalQuery = finalQuery.replace(":" + paramName, "'" + paramValue + "'");
                } else {
                    finalQuery = finalQuery.replace(":" + paramName, paramValue.toString());
                }
            }
        }

        return finalQuery;
    }

    private Map<String, Object> calculateSummary(List<Map<String, Object>> data) {
        // Implement summary calculation logic based on data
        // This is a simplified version
        return Map.of(
                "totalRecords", data.size(),
                "generatedAt", java.time.LocalDateTime.now()
        );
    }
}
