package com.itsutra.ai.project.service;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RequestIdGenerator {

    private static final String REQUEST_PREFIX = "REQ";
    private static final String ANALYSIS_PREFIX = "ANL";
    private static final String RESUME_PREFIX = "RES";
    private static final String QUESTION_PREFIX = "QST";
    private static final String BATCH_PREFIX = "BAT";

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyMMdd");
    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("HHmmssSSS");

    private final AtomicInteger sequenceCounter = new AtomicInteger(0);
    private final SecureRandom secureRandom = new SecureRandom();

    // ========== MAIN REQUEST ID GENERATION METHODS ==========

    /**
     * Generate a unique request ID for AI requests
     * Format: REQ_YYYYMMDD_HHMMSS_XXXXXX
     */
    public String generateRequestId() {
        LocalDateTime now = LocalDateTime.now();
        String datePart = now.format(DATE_FORMATTER);
        String timePart = now.format(TIMESTAMP_FORMATTER);
        String randomPart = generateRandomSuffix(6);

        return String.format("%s_%s_%s_%s",
                REQUEST_PREFIX, datePart, timePart, randomPart);
    }

    /**
     * Generate request ID with service type prefix
     */
    public String generateRequestId(String serviceType) {
        String prefix = getPrefixForService(serviceType);
        LocalDateTime now = LocalDateTime.now();
        String datePart = now.format(DATE_FORMATTER);
        String sequence = String.format("%04d", sequenceCounter.incrementAndGet() % 10000);

        return String.format("%s%s_%s_%s",
                prefix, datePart, sequence, generateRandomSuffix(4));
    }

    /**
     * Generate a short request ID (for URLs, logs)
     */
    public String generateShortRequestId() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return REQUEST_PREFIX + "_" + uuid.substring(0, 8).toUpperCase();
    }

    // ========== SERVICE-SPECIFIC ID GENERATION ==========

    public String generateInterviewAnalysisId() {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        String random = generateRandomSuffix(5);

        return String.format("%s_%s_%s", ANALYSIS_PREFIX, timestamp, random);
    }

    public String generateResumeAnalysisId() {
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DATE_FORMATTER);
        String sequence = String.format("%06d", sequenceCounter.incrementAndGet() % 1000000);

        return String.format("%s%s%s", RESUME_PREFIX, date, sequence);
    }

    public String generateQuestionSetId() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return QUESTION_PREFIX + "_" + uuid.substring(0, 12).toUpperCase();
    }

    public String generateBatchId() {
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DATE_FORMATTER);
        String time = now.format(TIMESTAMP_FORMATTER).substring(0, 6);

        return String.format("%s_%s_%s", BATCH_PREFIX, date, time);
    }

    // ========== VALIDATION AND PARSING METHODS ==========

    /**
     * Validate if a string is a valid request ID
     */
    public boolean isValidRequestId(String requestId) {
        if (requestId == null || requestId.length() < 10 || requestId.length() > 50) {
            return false;
        }

        // Check format patterns
        return requestId.matches("^[A-Z]{3}_[0-9]{6}_[0-9]{9}_[A-Z0-9]{6}$") ||  // REQ_YYMMDD_HHMMSSSSS_XXXXXX
                requestId.matches("^[A-Z]{3}[0-9]{6}_[0-9]{4}_[A-Z0-9]{4}$") ||   // PrefixYYMMDD_XXXX_XXXX
                requestId.matches("^[A-Z]{3}_[A-F0-9]{8}$") ||                    // REQ_XXXXXXXX
                requestId.matches("^[A-Z]{3}_[0-9]{12}_[A-Z0-9]{5}$") ||          // ANL_YYYYMMDDHHMM_XXXXX
                requestId.matches("^[A-Z]{3}[0-9]{6}[0-9]{6}$");                  // RESYYMMDDXXXXXX
    }

    /**
     * Extract timestamp from request ID
     */
    public LocalDateTime extractTimestamp(String requestId) {
        try {
            if (requestId.startsWith(REQUEST_PREFIX + "_")) {
                // Format: REQ_YYMMDD_HHMMSSSSS_XXXXXX
                String dateTimePart = requestId.substring(4, 20); // YYMMDD_HHMMSSSSS
                String dateStr = dateTimePart.substring(0, 6); // YYMMDD
                String timeStr = dateTimePart.substring(7);    // HHMMSSSSS

                int year = 2000 + Integer.parseInt(dateStr.substring(0, 2));
                int month = Integer.parseInt(dateStr.substring(2, 4));
                int day = Integer.parseInt(dateStr.substring(4, 6));
                int hour = Integer.parseInt(timeStr.substring(0, 2));
                int minute = Integer.parseInt(timeStr.substring(2, 4));
                int second = Integer.parseInt(timeStr.substring(4, 6));
                int nano = Integer.parseInt(timeStr.substring(6)) * 1000;

                return LocalDateTime.of(year, month, day, hour, minute, second, nano);
            } else if (requestId.matches("^[A-Z]{3}[0-9]{6}_[0-9]{4}_[A-Z0-9]{4}$")) {
                // Format: PrefixYYMMDD_XXXX_XXXX
                String dateStr = requestId.substring(3, 9); // YYMMDD
                int year = 2000 + Integer.parseInt(dateStr.substring(0, 2));
                int month = Integer.parseInt(dateStr.substring(2, 4));
                int day = Integer.parseInt(dateStr.substring(4, 6));

                return LocalDateTime.of(year, month, day, 0, 0);
            }
        } catch (Exception e) {
            // If parsing fails, return current time
        }
        return LocalDateTime.now();
    }

    /**
     * Extract service type from request ID
     */
    public String extractServiceType(String requestId) {
        if (requestId == null || requestId.length() < 3) {
            return "UNKNOWN";
        }

        String prefix = requestId.substring(0, 3);
//        return switch (prefix) {
//            case REQUEST_PREFIX -> "AI_REQUEST";
//            case ANALYSIS_PREFIX -> "INTERVIEW_ANALYSIS";
//            case RESUME_PREFIX -> "RESUME_ANALYSIS";
//            case QUESTION_PREFIX -> "QUESTION_GENERATION";
//            case BATCH_PREFIX -> "BATCH_PROCESSING";
//            default -> "UNKNOWN";
//        };
        return null;
    }

    // ========== HELPER METHODS ==========

    private String getPrefixForService(String serviceType) {
//        return switch (serviceType.toUpperCase()) {
//            case "INTERVIEW_ANALYSIS" -> ANALYSIS_PREFIX;
//            case "RESUME_PARSING", "RESUME_ANALYSIS" -> RESUME_PREFIX;
//            case "QUESTION_GENERATION" -> QUESTION_PREFIX;
//            case "BATCH_PROCESSING" -> BATCH_PREFIX;
//            default -> REQUEST_PREFIX;
//        };
        return null;
    }

    private String generateRandomSuffix(int length) {
        StringBuilder suffix = new StringBuilder(length);
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        for (int i = 0; i < length; i++) {
            int index = secureRandom.nextInt(characters.length());
            suffix.append(characters.charAt(index));
        }

        return suffix.toString();
    }

    /**
     * Generate a human-readable request ID
     */
    public String generateHumanReadableId(String serviceName, String userInitials) {
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String time = now.format(DateTimeFormatter.ofPattern("HHmm"));

        // Get first 3 letters of service name in uppercase
        String serviceCode = serviceName.length() >= 3 ?
                serviceName.substring(0, 3).toUpperCase() :
                String.format("%-3s", serviceName).toUpperCase().trim();

        // Use user initials or "SYS" for system
        String userCode = (userInitials != null && userInitials.length() >= 2) ?
                userInitials.substring(0, 2).toUpperCase() : "SYS";

        String random = generateRandomSuffix(3);

        return String.format("%s-%s-%s-%s-%s",
                serviceCode, date, time, userCode, random);
    }

    /**
     * Generate ID with checksum for validation
     */
    public String generateIdWithChecksum(String baseId) {
        int checksum = calculateChecksum(baseId);
        return baseId + "_" + String.format("%02X", checksum);
    }

    private int calculateChecksum(String input) {
        int sum = 0;
        for (char c : input.toCharArray()) {
            sum += (int) c;
        }
        return sum % 256;
    }

    /**
     * Verify checksum of an ID
     */
    public boolean verifyChecksum(String idWithChecksum) {
        if (idWithChecksum == null || !idWithChecksum.contains("_")) {
            return false;
        }

        int lastUnderscore = idWithChecksum.lastIndexOf("_");
        String baseId = idWithChecksum.substring(0, lastUnderscore);
        String checksumStr = idWithChecksum.substring(lastUnderscore + 1);

        try {
            int expectedChecksum = Integer.parseInt(checksumStr, 16);
            int actualChecksum = calculateChecksum(baseId);
            return expectedChecksum == actualChecksum;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // ========== BULK ID GENERATION ==========

    /**
     * Generate multiple unique request IDs
     */
    public List<String> generateRequestIds(int count) {
        List<String> ids = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            ids.add(generateRequestId());
        }
        return ids;
    }

    /**
     * Generate sequential IDs with prefix
     */
    public List<String> generateSequentialIds(String prefix, int start, int count) {
        List<String> ids = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            String sequence = String.format("%06d", start + i);
            ids.add(prefix + "_" + sequence);
        }
        return ids;
    }
}
