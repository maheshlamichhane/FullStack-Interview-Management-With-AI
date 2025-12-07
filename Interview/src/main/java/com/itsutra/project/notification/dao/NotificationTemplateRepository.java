package com.itsutra.project.notification.dao;

import com.itsutra.project.notification.entity.NotificationTemplate;
import com.itsutra.project.notification.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {

    Optional<NotificationTemplate> findByTemplateNameAndLanguageAndActiveTrue(
            String templateName, String language);

    Optional<NotificationTemplate> findByTemplateNameAndTypeAndLanguageAndActiveTrue(
            String templateName, NotificationType type, String language);
    Optional<NotificationTemplate> findFirstByTemplateNameAndTypeAndLanguageAndActiveTrueAndCreatedById(
            String templateName, NotificationType type, String language, Long id);

    boolean existsByTemplateNameAndLanguage(String templateName, String language);

    Optional<NotificationTemplate> findByIdAndCreatedById(Long id,Long userId);
    List<NotificationTemplate> findByCreatedById(Long userId);
    void deleteByIdAndCreatedById(Long id,Long userId);
}
