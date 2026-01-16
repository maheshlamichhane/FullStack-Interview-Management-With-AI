package com.notification.project.service;

import com.notification.project.dao.NotificationHistoryRepository;
import com.notification.project.dao.NotificationTemplateRepository;
import com.notification.project.dto.NotificationRequest;
import com.notification.project.dto.NotificationResponse;
import com.notification.project.entity.NotificationHistory;
import com.notification.project.enums.NotificationStatus;
import com.notification.project.mapper.NotificationMapper;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

public abstract class AbstractNotificationService implements NotificationService {

    protected final NotificationMapper notificationMapper;
    protected final NotificationHistoryRepository notificationHistoryRepository;
    protected final NotificationTemplateRepository notificationTemplateRepository;

    protected AbstractNotificationService(
            NotificationMapper notificationMapper,
            NotificationHistoryRepository notificationHistoryRepository,
            NotificationTemplateRepository notificationTemplateRepository
    ) {
        this.notificationMapper = notificationMapper;
        this.notificationHistoryRepository = notificationHistoryRepository;
        this.notificationTemplateRepository = notificationTemplateRepository;
    }

    @Override
    public Mono<NotificationResponse> manageHistory(NotificationRequest request) {

        NotificationHistory history = notificationMapper.toHistoryEntity(request);
        history.setReferenceId(generateReferenceId());
        history.setStatus(NotificationStatus.PENDING);
        history.setCreatedAt(LocalDateTime.now());

        // Reactive template processing
        return processTemplate(request)
                .flatMap(body -> sendNotification(request, body)
                        .flatMap(providerResponse -> {
                            history.setProviderResponse(providerResponse);
                            history.setStatus(NotificationStatus.SENT);
                            history.setDeliveredAt(LocalDateTime.now());
                            return notificationHistoryRepository.save(history);
                        }))
                .map(notificationMapper::toNotificationResponse);
    }

    private Mono<String> processTemplate(NotificationRequest request) {
        if (request.getTemplateName() != null) {
            return notificationTemplateRepository
                    .findFirstByTemplateNameAndTypeAndLanguageAndActiveTrueAndCreatedById(
                            request.getTemplateName(),
                            request.getType().toString(),
                            request.getLanguage(),
                            12345L
                    )
                    .switchIfEmpty(Mono.error(new RuntimeException("Template not found")))
                    .map(template -> replaceTemplateVariables(template.getBody(), request.getTemplateVariables()));
        }
        return Mono.just(request.getTemplateName() != null ? request.getTemplateName() : "");
    }

    private String replaceTemplateVariables(String template, Map<String, Object> variables) {
        if (variables == null || variables.isEmpty()) return template;

        String result = template;
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            result = result.replace("{{" + entry.getKey() + "}}", entry.getValue() != null ? entry.getValue().toString() : "");
        }
        return result;
    }

    private String generateReferenceId() {
        return "NOTIF_" + System.currentTimeMillis() + "_" + (int) (Math.random() * 1000);
    }
}
