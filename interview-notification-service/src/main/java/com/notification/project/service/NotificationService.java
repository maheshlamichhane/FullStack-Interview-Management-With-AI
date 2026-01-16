package com.notification.project.service;

import com.notification.project.dto.NotificationRequest;
import com.notification.project.dto.NotificationResponse;
import reactor.core.publisher.Mono;

public interface NotificationService {

    Mono<NotificationResponse> manageHistory(NotificationRequest request);

    Mono<String> sendNotification(NotificationRequest request, String body);
}
