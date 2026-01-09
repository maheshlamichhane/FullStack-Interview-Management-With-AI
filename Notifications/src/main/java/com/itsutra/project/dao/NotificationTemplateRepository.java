package com.itsutra.project.dao;

import com.itsutra.project.entity.NotificationTemplate;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface NotificationTemplateRepository extends ReactiveCrudRepository<NotificationTemplate, Long> {
    Mono<NotificationTemplate> findByIdAndCreatedById(Long id,Long userId);
    Flux<NotificationTemplate> findByCreatedById(Long userId);
}
