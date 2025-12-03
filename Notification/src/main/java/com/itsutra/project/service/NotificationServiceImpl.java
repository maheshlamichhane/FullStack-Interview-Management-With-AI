package com.itsutra.project.service;

import com.itsutra.project.dao.NotificationHistoryRepository;
import com.itsutra.project.dao.NotificationTemplateRepository;
import com.itsutra.project.dto.NotificationRequest;
import com.itsutra.project.dto.NotificationResponse;
import com.itsutra.project.entity.NotificationTemplate;
import com.itsutra.project.entity.NotificationHistory;
import com.itsutra.project.enums.NotificationStatus;
import com.itsutra.project.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationTemplateRepository templateRepository;
    private final NotificationHistoryRepository historyRepository;
    private final EmailService emailService;
    private final SmsService smsService;
    private final NotificationMapper mapper;

    @Override
    @Transactional
    public NotificationResponse sendNotification(NotificationRequest request) {
        try {

            // Save notification history
            NotificationHistory history = mapper.toHistoryEntity(request);
            history.setReferenceId(generateReferenceId());

            // Process based on notification type
            String providerResponse;

            switch (request.getType()) {
                case EMAIL:
                    providerResponse = emailService.sendEmail(
                            request.getRecipient(),
                            request.getSubject(),
                            processTemplate(request)
                    );
                    break;

                case SMS:
                    providerResponse = smsService.sendSms(
                            request.getRecipient(),
                            processTemplate(request)
                    );
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported notification type: " + request.getType());
            }

            // Update history with provider response
            history.setProviderResponse(providerResponse);
            history.setStatus(NotificationStatus.SENT);
            history.setDeliveredAt(LocalDateTime.now());

            NotificationHistory savedHistory = historyRepository.save(history);

            return mapper.toNotificationResponse(savedHistory);

        } catch (Exception e) {
            log.error("Failed to send notification: {}", e.getMessage(), e);

            // Save failed attempt
            NotificationHistory failedHistory = mapper.toHistoryEntity(request);
            failedHistory.setReferenceId(generateReferenceId());
            failedHistory.setStatus(NotificationStatus.FAILED);
            failedHistory.setProviderResponse(e.getMessage());
            historyRepository.save(failedHistory);

            throw new RuntimeException("Notification sending failed: " + e.getMessage(), e);
        }
    }

    @Override
    public void processPendingNotifications() {
        // Implementation for processing pending notifications in batch
        log.info("Processing pending notifications...");
    }

    private String processTemplate(NotificationRequest request) {
        if (request.getTemplateName() != null) {
            NotificationTemplate template = templateRepository
                    .findByTemplateNameAndTypeAndLanguageAndActiveTrue(
                            request.getTemplateName(),
                            request.getType(),
                            request.getLanguage()
                    )
                    .orElseThrow(() -> new RuntimeException("Template not found"));

            return replaceTemplateVariables(template.getBody(), request.getTemplateVariables());
        }

        return request.getMessage();
    }

    private String replaceTemplateVariables(String template, Map<String, Object> variables) {
        if (variables == null || variables.isEmpty()) {
            return template;
        }

        String processed = template;
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            processed = processed.replace(placeholder, entry.getValue() != null ? entry.getValue().toString() : "");
        }

        return processed;
    }

    private String generateReferenceId() {
        return "NOTIF_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }
}
