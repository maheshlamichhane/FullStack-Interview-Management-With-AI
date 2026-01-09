package com.itsutra.project.dao;

import com.itsutra.project.entity.NotificationTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationTemplateRepositoryTest {

    @Mock
    private NotificationTemplateRepository repository;

    @Test
    void findByIdAndCreatedById_shouldReturnTemplate() {
        Long templateId = 1L;
        Long userId = 123L;

        NotificationTemplate template = new NotificationTemplate();
        template.setId(templateId);
        template.setCreatedById(userId);

        when(repository.findByIdAndCreatedById(templateId, userId))
                .thenReturn(Mono.just(template));

        Mono<NotificationTemplate> result = repository.findByIdAndCreatedById(templateId, userId);

        StepVerifier.create(result)
                .expectNextMatches(t -> t.getId().equals(templateId) && t.getCreatedById() == userId)
                .verifyComplete();
    }

    @Test
    void findByCreatedById_shouldReturnFluxOfTemplates() {
        Long userId = 123L;

        NotificationTemplate template1 = new NotificationTemplate();
        template1.setId(1L);
        template1.setCreatedById(userId);

        NotificationTemplate template2 = new NotificationTemplate();
        template2.setId(2L);
        template2.setCreatedById(userId);

        when(repository.findByCreatedById(userId))
                .thenReturn(Flux.just(template1, template2));

        StepVerifier.create(repository.findByCreatedById(userId))
                .expectNext(template1)
                .expectNext(template2)
                .verifyComplete();
    }
}

