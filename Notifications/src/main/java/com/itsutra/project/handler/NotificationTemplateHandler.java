package com.itsutra.project.handler;


import com.itsutra.project.dao.NotificationTemplateRepository;
import com.itsutra.project.dto.TemplateRequest;
import com.itsutra.project.dto.TemplateResponse;
import com.itsutra.project.entity.NotificationTemplate;
import com.itsutra.project.exception.NotificationTemplateNotFoundException;
import com.itsutra.project.mapper.NotificationMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.function.Function;

import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Component
public class NotificationTemplateHandler {

    private  NotificationTemplateRepository notificationTemplateRepository;
    private  NotificationMapper notificationMapper;
    public long userId = 12345l;


    public NotificationTemplateHandler(NotificationTemplateRepository notificationTemplateRepository, NotificationMapper notificationMapper) {
        this.notificationTemplateRepository = notificationTemplateRepository;
        this.notificationMapper = notificationMapper;
    }

    public Mono<ServerResponse> createTemplate(ServerRequest request) {
        return request.bodyToMono(TemplateRequest.class)
                .flatMap(saveTemplateFunction)
                .flatMap(response -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .bodyValue(response)
                );
    }

    private final Function<TemplateRequest, Mono<TemplateResponse>> saveTemplateFunction = templateRequest -> {
        NotificationTemplate template = notificationMapper.toTemplateEntity(templateRequest);
        template.setCreatedById(userId);
        return notificationTemplateRepository.save(template)
                .map(notificationMapper::toTemplateResponse);
    };



    public Mono<ServerResponse> updateTemplate(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));

        return request.bodyToMono(TemplateRequest.class)
                .flatMap(templateRequest ->
                        notificationTemplateRepository.findByIdAndCreatedById(id, userId)
                                .switchIfEmpty(Mono.error(new NotificationTemplateNotFoundException(id.intValue())))
                                .flatMap(template -> {
                                    template.setSubject(templateRequest.getSubject());
                                    template.setBody(templateRequest.getBody());
                                    template.setActive(true);
                                    return notificationTemplateRepository.save(template);
                                })
                                .map(notificationMapper::toTemplateResponse)
                )
                .flatMap(response -> ServerResponse
                        .status(HttpStatus.OK)
                        .bodyValue(response)
                );
    }



    public Mono<ServerResponse> getTemplateById(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));

        return notificationTemplateRepository.findByIdAndCreatedById(id,userId)
                .switchIfEmpty(Mono.error(new NotificationTemplateNotFoundException(id.intValue())))
                .flatMap(response -> ok().bodyValue(response));
    }


    public Mono<ServerResponse> getAllTemplates(ServerRequest request) {
        Flux<NotificationTemplate> templates = notificationTemplateRepository.findByCreatedById(userId);
        return ok().body(templates, TemplateResponse.class);
    }

    public Mono<ServerResponse> deleteTemplate(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
       return notificationTemplateRepository.findByIdAndCreatedById(id, userId)
               .switchIfEmpty(Mono.error(new NotificationTemplateNotFoundException(id.intValue())))
               .flatMap(template -> notificationTemplateRepository.delete(template))
               .then(noContent().build());
    }


}




