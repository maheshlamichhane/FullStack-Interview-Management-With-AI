package com.interview.project.service;

import com.interview.project.dto.NotificationRequest;
import com.interview.project.dto.NotificationResponse;
import reactor.core.publisher.Mono;

public interface NotificationService {

    Mono<NotificationResponse> manageHistory(NotificationRequest request);

    Mono<String> sendNotification(NotificationRequest request, String body);
}
