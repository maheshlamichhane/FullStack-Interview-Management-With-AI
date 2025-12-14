package com.itsutra.project.dao;

import com.itsutra.project.entity.NotificationHistory;
import com.itsutra.project.enums.NotificationStatus;
import com.itsutra.project.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationHistoryRepository extends JpaRepository<NotificationHistory, Long> {

    Optional<NotificationHistory> findByReferenceId(String referenceId);

    List<NotificationHistory> findByRecipient(String recipient);

    List<NotificationHistory> findByType(NotificationType type);

    List<NotificationHistory> findByStatus(NotificationStatus status);

    List<NotificationHistory> findBySentAtBetween(LocalDateTime start, LocalDateTime end);

    long countByRecipientAndStatus(String recipient, NotificationStatus status);
}
