package com.itsutra.project.service;

import com.itsutra.project.dto.NotificationRequest;
import com.itsutra.project.dto.NotificationResponse;
import reactor.core.publisher.Mono;

public interface NotificationService {

    Mono<NotificationResponse> manageHistory(NotificationRequest request);

    Mono<String> sendNotification(NotificationRequest request, String body);
}
