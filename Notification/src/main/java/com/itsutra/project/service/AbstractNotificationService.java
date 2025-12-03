package com.itsutra.project.service;

import com.itsutra.project.dao.NotificationHistoryRepository;
import com.itsutra.project.dao.NotificationTemplateRepository;
import com.itsutra.project.dto.NotificationRequest;
import com.itsutra.project.dto.NotificationResponse;
import com.itsutra.project.entity.NotificationHistory;
import com.itsutra.project.entity.NotificationTemplate;
import com.itsutra.project.entity.User;
import com.itsutra.project.enums.NotificationStatus;
import com.itsutra.project.mapper.NotificationMapper;

import java.time.LocalDateTime;
import java.util.Map;


public abstract class AbstractNotificationService implements NotificationService {


    private final NotificationMapper notificationMapper;


    private final NotificationHistoryRepository notificationHistoryRepository;

    private final NotificationTemplateRepository notificationTemplateRepository;

    private final AuthenticationService authenticationService;

    public AbstractNotificationService(NotificationMapper notificationMapper, NotificationHistoryRepository notificationHistoryRepository, NotificationTemplateRepository notificationTemplateRepository, AuthenticationService authenticationService) {
        this.notificationMapper = notificationMapper;
        this.notificationHistoryRepository = notificationHistoryRepository;
        this.notificationTemplateRepository = notificationTemplateRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public NotificationResponse manageHistory(NotificationRequest notificationRequest) {
        NotificationHistory history = notificationMapper.toHistoryEntity(notificationRequest);
        history.setReferenceId(generateReferenceId());

        String body = processTemplate(notificationRequest);
        String providerResponse = sendNotification(notificationRequest,body);


        // Update history with provider response
        history.setProviderResponse(providerResponse);
        history.setStatus(NotificationStatus.SENT);
        history.setDeliveredAt(LocalDateTime.now());

        NotificationHistory savedHistory =  notificationHistoryRepository.save(history);
        return notificationMapper.toNotificationResponse(savedHistory);
    }

        private String processTemplate(NotificationRequest request) {
        User user = authenticationService.getCurrentUser();
        if (request.getTemplateName() != null) {
            NotificationTemplate template = notificationTemplateRepository
                    .findFirstByTemplateNameAndTypeAndLanguageAndActiveTrueAndCreatedById(
                            request.getTemplateName(),
                            request.getType(),
                            request.getLanguage(),
                            user.getId()

                    )
                    .orElseThrow(() -> new RuntimeException("Template not found"));

            return replaceTemplateVariables(template.getBody(), request.getTemplateVariables());
        }
        return "";

//        return request.getMessage();
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
