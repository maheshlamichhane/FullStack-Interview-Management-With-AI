package com.notification.project.dao;

import com.notification.project.entity.NotificationHistory;
import com.notification.project.enums.NotificationStatus;
import com.notification.project.enums.NotificationType;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
public interface NotificationHistoryRepository extends ReactiveCrudRepository<NotificationHistory, Long> {

    Mono<NotificationHistory> findByReferenceId(String referenceId);

    Flux<NotificationHistory> findByRecipient(String recipient);

    Flux<NotificationHistory> findByType(NotificationType type);

    Flux<NotificationHistory> findByStatus(NotificationStatus status);

    Flux<NotificationHistory> findBySentAtBetween(LocalDateTime start, LocalDateTime end);

    long countByRecipientAndStatus(String recipient, NotificationStatus status);
}
