package com.notification.project.service;

import com.notification.project.dao.NotificationHistoryRepository;
import com.notification.project.dao.NotificationTemplateRepository;
import com.notification.project.dto.NotificationRequest;
import com.notification.project.mapper.NotificationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@Qualifier("smsNotificationService")
public class SMSNotificationServiceImpl extends AbstractNotificationService {

    public SMSNotificationServiceImpl(NotificationMapper notificationMapper,
                                      NotificationHistoryRepository notificationHistoryRepository,
                                      NotificationTemplateRepository notificationTemplateRepository) {
        super(notificationMapper, notificationHistoryRepository, notificationTemplateRepository);
    }

    @Override
    public Mono<String> sendNotification(NotificationRequest request, String body) {
        return Mono.fromCallable(() -> {
            log.info("SMS sent successfully to {}", request.getRecipient());
            return "SUCCESS";
        }).onErrorResume(e -> {
            log.error("Failed to send SMS to {}: {}", request.getRecipient(), e.getMessage(), e);
            return Mono.just("FAILED: " + e.getMessage());
        });
    }
}
