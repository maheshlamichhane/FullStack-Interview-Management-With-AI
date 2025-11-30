package com.itsutra.project.service;

import com.itsutra.project.dto.ExportRequestDTO;
import com.itsutra.project.dto.ReportDataResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExportService {

    private final ReportService reportService;
    private final DashboardService dashboardService;

    /**
     * Export report data in various formats
     */
    public Resource exportReport(Long reportId, ExportRequestDTO request) {
        log.info("Exporting report {} in {} format", reportId, request.getFormat());

        try {
            Map<String, Object> parameters = request.getFilters() != null ? request.getFilters() : Map.of();
            ReportDataResponseDTO reportData = reportService.getReportData(reportId, parameters);

            return switch (request.getFormat().toUpperCase()) {
                case "CSV" -> exportToCsv(reportData, getExportFileName(reportId, "csv"));
                case "EXCEL" -> exportToExcel(reportData, getExportFileName(reportId, "xlsx"));
                case "PDF" -> exportToPdf(reportData, getExportFileName(reportId, "pdf"));
                case "JSON" -> exportToJson(reportData, getExportFileName(reportId, "json"));
                default -> throw new IllegalArgumentException("Unsupported export format: " + request.getFormat());
            };

        } catch (Exception e) {
            log.error("Error exporting report {}", reportId, e);
            throw new RuntimeException("Export failed: " + e.getMessage(), e);
        }
    }

    /**
     * Export dashboard data
     */
    public Resource exportDashboard(Long dashboardId, ExportRequestDTO request) {
        log.info("Exporting dashboard {} in {} format", dashboardId, request.getFormat());

        try {
            Map<String, Object> dashboardData = dashboardService.getDashboardData(dashboardId);

            // For now, export as JSON. Could be enhanced to export visualizations as images
            return exportToJson(dashboardData, getExportFileName(dashboardId, request.getFormat()));

        } catch (Exception e) {
            log.error("Error exporting dashboard {}", dashboardId, e);
            throw new RuntimeException("Export failed: " + e.getMessage(), e);
        }
    }

    /**
     * Export report data to CSV format
     */
    private Resource exportToCsv(ReportDataResponseDTO reportData, String fileName) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            StringBuilder csvContent = new StringBuilder();

            // Add header
            if (reportData.getColumns() != null && !reportData.getColumns().isEmpty()) {
                List<String> headers = reportData.getColumns().stream()
                        .map(column -> column.getDisplayName() != null ? column.getDisplayName() : column.getName())
                        .toList();
                csvContent.append(String.join(",", headers)).append("\n");
            }

            // Add data rows
            if (reportData.getData() != null) {
                for (Map<String, Object> row : reportData.getData()) {
                    List<String> rowValues = reportData.getColumns().stream()
                            .map(column -> {
                                Object value = row.get(column.getName());
                                return escapeCsvValue(value != null ? value.toString() : "");
                            })
                            .toList();
                    csvContent.append(String.join(",", rowValues)).append("\n");
                }
            }

            outputStream.write(csvContent.toString().getBytes());
            return new ByteArrayResource(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("CSV export failed", e);
        }
    }

    /**
     * Export report data to Excel format
     */
    private Resource exportToExcel(ReportDataResponseDTO reportData, String fileName) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Report Data");

            // Create header row
            Row headerRow = sheet.createRow(0);
            if (reportData.getColumns() != null) {
                for (int i = 0; i < reportData.getColumns().size(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(reportData.getColumns().get(i).getDisplayName() != null ?
                            reportData.getColumns().get(i).getDisplayName() :
                            reportData.getColumns().get(i).getName());

                    // Style the header
                    CellStyle headerStyle = workbook.createCellStyle();
                    Font headerFont = workbook.createFont();
                    headerFont.setBold(true);
                    headerStyle.setFont(headerFont);
                    headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    cell.setCellStyle(headerStyle);
                }
            }

            // Create data rows
            if (reportData.getData() != null) {
                int rowNum = 1;
                for (Map<String, Object> row : reportData.getData()) {
                    Row dataRow = sheet.createRow(rowNum++);
                    if (reportData.getColumns() != null) {
                        for (int i = 0; i < reportData.getColumns().size(); i++) {
                            Cell cell = dataRow.createCell(i);
                            Object value = row.get(reportData.getColumns().get(i).getName());
                            setCellValue(cell, value);
                        }
                    }
                }
            }

            // Auto-size columns
            if (reportData.getColumns() != null) {
                for (int i = 0; i < reportData.getColumns().size(); i++) {
                    sheet.autoSizeColumn(i);
                }
            }

            workbook.write(outputStream);
            return new ByteArrayResource(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Excel export failed", e);
        }
    }

    /**
     * Export to PDF (simplified - would use a PDF library like iText or Apache PDFBox)
     */
    private Resource exportToPdf(ReportDataResponseDTO reportData, String fileName) {
        // This is a simplified implementation
        // In production, you would use a PDF generation library

        String pdfContent = generatePdfContent(reportData);
        return new ByteArrayResource(pdfContent.getBytes());
    }

    /**
     * Export to JSON format
     */
    private Resource exportToJson(Object data, String fileName) {
        try {
            // Using Jackson ObjectMapper from the mapper class
            String jsonContent = new ObjectMapper().writeValueAsString(data);
            return new ByteArrayResource(jsonContent.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("JSON export failed", e);
        }
    }

    /**
     * Export report data for execution (used in ReportService)
     */
    public String exportReportData(ReportDataResponseDTO data, String format) {
        try {
            Resource exportResource = switch (format.toUpperCase()) {
                case "CSV" -> exportToCsv(data, "export.csv");
                case "EXCEL" -> exportToExcel(data, "export.xlsx");
                case "PDF" -> exportToPdf(data, "export.pdf");
                default -> throw new IllegalArgumentException("Unsupported format: " + format);
            };

            // In a real implementation, you would save this to a file storage service
            // and return the URL. For now, return a placeholder URL.
            return "/exports/" + System.currentTimeMillis() + "." + format.toLowerCase();

        } catch (Exception e) {
            log.error("Error exporting report data", e);
            throw new RuntimeException("Export failed: " + e.getMessage(), e);
        }
    }

    // Helper methods

    private String escapeCsvValue(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private void setCellValue(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof LocalDateTime) {
            cell.setCellValue(((LocalDateTime) value).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        } else {
            cell.setCellValue(value.toString());
        }
    }

    private String generatePdfContent(ReportDataResponseDTO reportData) {
        // Simplified PDF content generation
        // In production, use a proper PDF library

        StringBuilder pdfContent = new StringBuilder();
        pdfContent.append("PDF Report\n");
        pdfContent.append("Generated: ").append(LocalDateTime.now()).append("\n\n");

        if (reportData.getColumns() != null) {
            // Add headers
            for (var column : reportData.getColumns()) {
                pdfContent.append(column.getDisplayName()).append("\t");
            }
            pdfContent.append("\n");

            // Add data
            if (reportData.getData() != null) {
                for (Map<String, Object> row : reportData.getData()) {
                    for (var column : reportData.getColumns()) {
                        Object value = row.get(column.getName());
                        pdfContent.append(value != null ? value.toString() : "").append("\t");
                    }
                    pdfContent.append("\n");
                }
            }
        }

        return pdfContent.toString();
    }

    private String getExportFileName(Long id, String format) {
        return "export_" + id + "_" + System.currentTimeMillis() + "." + format.toLowerCase();
    }
}
