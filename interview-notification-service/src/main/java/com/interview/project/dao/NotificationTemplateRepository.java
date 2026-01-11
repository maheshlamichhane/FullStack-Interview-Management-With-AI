package com.interview.project.dao;

import com.interview.project.entity.NotificationTemplate;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface NotificationTemplateRepository extends ReactiveCrudRepository<NotificationTemplate, Long> {
    Mono<NotificationTemplate> findByIdAndCreatedById(Long id, Long userId);
    Flux<NotificationTemplate> findByCreatedById(Long userId);
    Mono<NotificationTemplate> findFirstByTemplateNameAndTypeAndLanguageAndActiveTrueAndCreatedById(String templateName,String type,String language,Long createdById);
}
