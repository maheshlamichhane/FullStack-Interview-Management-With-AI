package com.itsutra.project.dao;

import com.itsutra.project.entity.NotificationTemplate;
import com.itsutra.project.enums.NotificationType;
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

    boolean existsByTemplateNameAndLanguage(String templateName, String language);

    Optional<NotificationTemplate> findByIdAndCreatedById(Long id,Long userId);
    List<NotificationTemplate> findByCreatedById(Long userId);
    void deleteByIdAndCreatedById(Long id,Long userId);
}
