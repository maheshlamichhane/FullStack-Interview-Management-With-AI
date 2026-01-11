package com.interview.project.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.project.dto.NotificationRequest;
import com.interview.project.dto.NotificationResponse;
import com.interview.project.dto.TemplateRequest;
import com.interview.project.dto.TemplateResponse;
import com.interview.project.entity.NotificationHistory;
import com.interview.project.entity.NotificationTemplate;
import com.interview.project.enums.NotificationStatus;
import com.interview.project.enums.NotificationType;
import org.springframework.boot.json.JsonParseException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.interview.project.enums.NotificationType.EMAIL;
import static com.interview.project.enums.NotificationType.SMS;

@Component
public class NotificationMapper {

    /**
     * Convert TemplateRequest to NotificationTemplate entity
     */
    public NotificationTemplate toTemplateEntity(TemplateRequest request) {
        if (request == null) {
            return null;
        }
        NotificationTemplate template = new NotificationTemplate();
        template.setTemplateName(request.getTemplateName());
        template.setSubject(request.getSubject());
        template.setBody(request.getBody());
        template.setType(request.getType());
        template.setLanguage(request.getLanguage());
        template.setActive(true);
        template.setCreatedAt(LocalDateTime.now());
        template.setUpdatedAt(LocalDateTime.now());

        return template;
    }

    /**
     * Update existing NotificationTemplate with TemplateRequest data
     */
    public NotificationTemplate updateTemplateFromRequest(
            NotificationTemplate existingTemplate,
            TemplateRequest request) {

        if (existingTemplate == null || request == null) {
            return existingTemplate;
        }

        if (request.getSubject() != null) {
            existingTemplate.setSubject(request.getSubject());
        }

        if (request.getBody() != null) {
            existingTemplate.setBody(request.getBody());
        }

        if (request.getType() != null) {
            existingTemplate.setType(request.getType());
        }

        if (request.getLanguage() != null) {
            existingTemplate.setLanguage(request.getLanguage());
        }

        existingTemplate.setUpdatedAt(LocalDateTime.now());

        return existingTemplate;
    }

    /**
     * Convert NotificationRequest to NotificationHistory entity
     */
    public NotificationHistory toHistoryEntity(NotificationRequest request) {
        if (request == null) {
            return null;
        }

        NotificationHistory history = new NotificationHistory();
        history.setRecipient(request.getRecipient());
        history.setSubject(request.getSubject());
//        history.setMessage(request.getMessage());
        history.setType(request.getType());
        history.setStatus(NotificationStatus.PENDING);
        history.setSentAt(LocalDateTime.now());
        history.setReferenceId(generateReferenceId());

        // Process template variables if template is used
        if (request.getTemplateName() != null && request.getTemplateVariables() != null) {
            history.setProviderResponse("Template variables: " +
                    request.getTemplateVariables().toString());
        }

        return history;
    }

    /**
     * Convert NotificationHistory to NotificationResponse DTO
     */
    public NotificationResponse toNotificationResponse(NotificationHistory history) {
        if (history == null) {
            return null;
        }

        NotificationResponse response = new NotificationResponse();
        response.setNotificationId(String.valueOf(history.getId()));
        response.setType(history.getType());
        response.setRecipient(history.getRecipient());
        response.setStatus(history.getStatus());
        response.setProviderResponse(history.getProviderResponse());
        response.setReferenceId(history.getReferenceId());
        response.setSentAt(history.getSentAt());
        response.setDeliveredAt(history.getDeliveredAt());

        return response;
    }

    /**
     * Convert NotificationTemplate to TemplateResponse DTO
     */
    public TemplateResponse toTemplateResponse(NotificationTemplate template) {
        if (template == null) {
            return null;
        }

        TemplateResponse response = new TemplateResponse();
        response.setId(template.getId());
        response.setTemplateName(template.getTemplateName());
        response.setSubject(template.getSubject());
        response.setBody(template.getBody());
        response.setType(template.getType());
        response.setLanguage(template.getLanguage());
        response.setActive(template.isActive());
        response.setCreatedAt(template.getCreatedAt());
        response.setUpdatedAt(template.getUpdatedAt());

        return response;
    }

    /**
     * Map list of entities to DTOs
     */
    public List<NotificationResponse> toNotificationResponseList(List<NotificationHistory> histories) {
        if (histories == null || histories.isEmpty()) {
            return Collections.emptyList();
        }

        List<NotificationResponse> responses = new ArrayList<>();
        for (NotificationHistory history : histories) {
            responses.add(toNotificationResponse(history));
        }

        return responses;
    }

    /**
     * Map paginated results
     */
    public List<NotificationResponse> toNotificationResponsePage(List<NotificationHistory> historyPage) {
        if (historyPage == null) {
            return new ArrayList<>();
        }

        List<NotificationResponse> responses = toNotificationResponseList(historyPage);

//        return new PageImpl<>(
//                responses,
//                historyPage.getPageable(),
//                historyPage.getTotalElements()
//        );
        return null;
    }

    /**
     * Process template with variables
     */
    public String processTemplate(String templateBody, Map<String, Object> variables) {
        if (templateBody == null) {
            return null;
        }

        if (variables == null || variables.isEmpty()) {
            return templateBody;
        }

        String processedContent = templateBody;

        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            String replacement = entry.getValue() != null ? entry.getValue().toString() : "";
            processedContent = processedContent.replace(placeholder, replacement);

            // Alternative placeholder format: ${key}
            String altPlaceholder = "\\$\\{" + entry.getKey() + "\\}";
            processedContent = processedContent.replaceAll(altPlaceholder, replacement);
        }

        return processedContent;
    }

    /**
     * Prepare notification message from template and variables
     */
    public String prepareNotificationMessage(NotificationTemplate template,
                                             NotificationRequest request) {
        if (template == null) {
//            return request.getMessage();
        }

        String processedBody = processTemplate(template.getBody(), request.getTemplateVariables());

        // If request has custom message, append it to template body
//        if (request.getMessage() != null && !request.getMessage().trim().isEmpty()) {
//            return processedBody + "\n\n" + request.getMessage();
//        }

        return processedBody;
    }

    /**
     * Prepare notification subject from template and request
     */
    public String prepareNotificationSubject(NotificationTemplate template,
                                             NotificationRequest request) {
        if (template == null || template.getSubject() == null) {
            return request.getSubject();
        }

        return processTemplate(template.getSubject(), request.getTemplateVariables());
    }

    /**
     * Map notification type from string
     */
    public NotificationType mapNotificationType(String typeString) {
        if (typeString == null || typeString.trim().isEmpty()) {
            throw new IllegalArgumentException("Notification type cannot be null or empty");
        }

        try {
            return NotificationType.valueOf(typeString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid notification type: " + typeString +
                    ". Valid values are: " + Arrays.toString(NotificationType.values()));
        }
    }

    /**
     * Map notification status from string
     */
    public NotificationStatus mapNotificationStatus(String statusString) {
        if (statusString == null || statusString.trim().isEmpty()) {
            return NotificationStatus.PENDING;
        }

        try {
            return NotificationStatus.valueOf(statusString.toUpperCase());
        } catch (IllegalArgumentException e) {
            return NotificationStatus.PENDING;
        }
    }

    /**
     * Generate unique reference ID for notifications
     */
    private String generateReferenceId() {
        return "NOTIF-" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")) +
                "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * Validate NotificationRequest
     */
    public void validateNotificationRequest(NotificationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Notification request cannot be null");
        }

        if (request.getType() == null) {
            throw new IllegalArgumentException("Notification type is required");
        }

        if (request.getRecipient() == null || request.getRecipient().trim().isEmpty()) {
            throw new IllegalArgumentException("Recipient is required");
        }

        // Validate based on notification type
        switch (request.getType()) {
            case EMAIL:
                if (!isValidEmail(request.getRecipient())) {
                    throw new IllegalArgumentException("Invalid email address: " + request.getRecipient());
                }
                break;

            case SMS:
                if (!isValidPhoneNumber(request.getRecipient())) {
                    throw new IllegalArgumentException("Invalid phone number: " + request.getRecipient());
                }
                break;

            default:
                // For other types, basic validation
                break;
        }

        // Validate template name and variables
        if (request.getTemplateName() != null && request.getTemplateVariables() == null) {
            request.setTemplateVariables(new HashMap<>());
        }
    }

    /**
     * Validate TemplateRequest
     */
    public void validateTemplateRequest(TemplateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Template request cannot be null");
        }

        if (request.getTemplateName() == null || request.getTemplateName().trim().isEmpty()) {
            throw new IllegalArgumentException("Template name is required");
        }

        if (request.getBody() == null || request.getBody().trim().isEmpty()) {
            throw new IllegalArgumentException("Template body is required");
        }

        if (request.getType() == null) {
            throw new IllegalArgumentException("Template type is required");
        }

        if (request.getLanguage() == null || request.getLanguage().trim().isEmpty()) {
            request.setLanguage("en");
        }
    }

    /**
     * Helper method to validate email format
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email != null && email.matches(emailRegex);
    }

    /**
     * Helper method to validate phone number format
     */
    private boolean isValidPhoneNumber(String phoneNumber) {
        // Basic phone number validation - can be enhanced based on requirements
        String phoneRegex = "^[+]?[0-9]{10,15}$";
        return phoneNumber != null && phoneNumber.replaceAll("\\s+", "").matches(phoneRegex);
    }

    /**
     * Convert map to JSON string for storage
     */
    public String convertMapToJson(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert map to JSON", e);
        }
    }

    /**
     * Convert JSON string to map
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> convertJsonToMap(String json) {
        if (json == null || json.trim().isEmpty()) {
            return new HashMap<>();
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, Map.class);
        } catch (JsonParseException e) {
            throw new RuntimeException("Failed to convert JSON to map", e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}