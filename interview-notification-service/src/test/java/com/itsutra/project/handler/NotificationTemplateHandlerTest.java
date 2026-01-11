package com.itsutra.project.handler;


import com.itsutra.project.dao.NotificationTemplateRepository;
import com.itsutra.project.dto.TemplateRequest;
import com.itsutra.project.dto.TemplateResponse;
import com.itsutra.project.entity.NotificationTemplate;
import com.itsutra.project.exception.NotificationTemplateNotFoundException;
import com.itsutra.project.mapper.NotificationMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.web.reactive.function.BodyExtractors;

@ExtendWith(MockitoExtension.class)
class NotificationTemplateHandlerTest {

    @Mock
    private NotificationTemplateRepository notificationTemplateRepository;

    @Mock
    private NotificationMapper notificationMapper;

    @InjectMocks
    private NotificationTemplateHandler notificationTemplateHandler;

    @Test
    void createTemplate_shouldReturnCreatedResponse() {
        // GIVEN
        TemplateRequest requestDto = new TemplateRequest();
        requestDto.setTemplateName("WELCOME");

        NotificationTemplate entity = new NotificationTemplate();
        entity.setId(10L);
        entity.setCreatedById(12345L);

        TemplateResponse responseDto = new TemplateResponse();
        responseDto.setId(10L);
        responseDto.setTemplateName("WELCOME");

        when(notificationMapper.toTemplateEntity(requestDto)).thenReturn(entity);
        when(notificationTemplateRepository.save(any(NotificationTemplate.class)))
                .thenReturn(Mono.just(entity));
        when(notificationMapper.toTemplateResponse(entity)).thenReturn(responseDto);

        ServerRequest serverRequest =
                MockServerRequest.builder()
                        .body(Mono.just(requestDto));

        // WHEN
        Mono<ServerResponse> responseMono =
                notificationTemplateHandler.createTemplate(serverRequest);

        // THEN
        StepVerifier.create(responseMono)
                .assertNext(serverResponse ->
                        assertThat(serverResponse.statusCode())
                                .isEqualTo(HttpStatus.CREATED)
                )
                .verifyComplete();

        verify(notificationTemplateRepository).save(any(NotificationTemplate.class));
    }



    @Test
    void updateTemplate_shouldReturnOkResponse() {
        // GIVEN
        Long templateId = 12345l;

        TemplateRequest requestDto = new TemplateRequest();
        requestDto.setSubject("Updated Subject");
        requestDto.setBody("Updated Body");

        NotificationTemplate existingTemplate = new NotificationTemplate();
        existingTemplate.setId(templateId);
        existingTemplate.setCreatedById(12345l);

        TemplateResponse responseDto = new TemplateResponse();
        responseDto.setId(templateId);
        responseDto.setSubject("Updated Subject");

        when(notificationTemplateRepository.findByIdAndCreatedById(templateId, 12345l))
                .thenReturn(Mono.just(existingTemplate));

        when(notificationTemplateRepository.save(any(NotificationTemplate.class)))
                .thenReturn(Mono.just(existingTemplate));

        when(notificationMapper.toTemplateResponse(existingTemplate))
                .thenReturn(responseDto);

        ServerRequest serverRequest = MockServerRequest.builder()
                .pathVariable("id", templateId.toString())
                .body(Mono.just(requestDto));

        // WHEN
        Mono<ServerResponse> responseMono = notificationTemplateHandler.updateTemplate(serverRequest);

        // THEN
        StepVerifier.create(responseMono)
                .assertNext(serverResponse ->
                        assertThat(serverResponse.statusCode()).isEqualTo(HttpStatus.OK)
                )
                .verifyComplete();

        verify(notificationTemplateRepository).findByIdAndCreatedById(templateId, 12345l);
        verify(notificationTemplateRepository).save(any(NotificationTemplate.class));
    }



    @Test
    void getTemplateById_shouldReturnOkResponse() {
        // GIVEN
        Long templateId = 10L;

        NotificationTemplate template = new NotificationTemplate();
        template.setId(templateId);
        template.setCreatedById(10L);

        when(notificationTemplateRepository.findByIdAndCreatedById(templateId, 12345L))
                .thenReturn(Mono.just(template));

        ServerRequest serverRequest = MockServerRequest.builder()
                .pathVariable("id", templateId.toString())
                .build();

        // WHEN
        Mono<ServerResponse> responseMono =
                notificationTemplateHandler.getTemplateById(serverRequest);

        // THEN
        StepVerifier.create(responseMono)
                .assertNext(serverResponse ->
                        assertThat(serverResponse.statusCode()).isEqualTo(HttpStatus.OK)
                )
                .verifyComplete();

        verify(notificationTemplateRepository)
                .findByIdAndCreatedById(templateId, 12345L);
    }

    @Test
    void getAllTemplates_shouldReturnAllTemplates() {

        // GIVEN
        NotificationTemplate t1 = new NotificationTemplate();
        t1.setId(1L);
        t1.setTemplateName("WELCOME");

        NotificationTemplate t2 = new NotificationTemplate();
        t2.setId(2L);
        t2.setTemplateName("GOODBYE");

        when(notificationTemplateRepository.findByCreatedById(12345l))
                .thenReturn(Flux.fromIterable(List.of(t1, t2)));

        MockServerRequest request = MockServerRequest.builder().build();

        // WHEN
        Mono<ServerResponse> responseMono = notificationTemplateHandler.getAllTemplates(request);

        Flux<NotificationTemplate> bodyFlux = notificationTemplateRepository.findByCreatedById(12345l);

        // THEN
        StepVerifier.create(bodyFlux)
                .expectNext(t1)
                .expectNext(t2)
                .verifyComplete();
    }

    @Test
    void deleteTemplate_shouldReturnNoContent_whenTemplateExists() {
        // GIVEN
        Long templateId = 1L;

        NotificationTemplate template = new NotificationTemplate();
        template.setId(templateId);
        template.setCreatedById(12345l);

        when(notificationTemplateRepository.findByIdAndCreatedById(templateId, 12345l))
                .thenReturn(Mono.just(template));
        when(notificationTemplateRepository.delete(template))
                .thenReturn(Mono.empty());

        ServerRequest request = MockServerRequest.builder()
                .pathVariable("id", String.valueOf(templateId))
                .build();

        // WHEN
        Mono<ServerResponse> responseMono = notificationTemplateHandler.deleteTemplate(request);

        // THEN
        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT);
                })
                .verifyComplete();

        verify(notificationTemplateRepository).delete(template);
    }

    @Test
    void deleteTemplate_shouldThrowNotFound_whenTemplateDoesNotExist() {
        // GIVEN
        Long templateId = 1L;

        when(notificationTemplateRepository.findByIdAndCreatedById(templateId, 12345l))
                .thenReturn(Mono.empty());

        ServerRequest request = MockServerRequest.builder()
                .pathVariable("id", String.valueOf(templateId))
                .build();

        // WHEN
        Mono<ServerResponse> responseMono = notificationTemplateHandler.deleteTemplate(request);

        // THEN
        StepVerifier.create(responseMono)
                .expectErrorMatches(throwable ->
                        throwable instanceof NotificationTemplateNotFoundException &&
                                ((NotificationTemplateNotFoundException) throwable).getMessage().contains(templateId.toString())
                )
                .verify();
    }


}

