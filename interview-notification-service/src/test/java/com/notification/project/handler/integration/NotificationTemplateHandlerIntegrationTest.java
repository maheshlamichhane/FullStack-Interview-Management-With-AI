//package com.notification.project.handler.integration;
//
//
//import com.notification.project.dao.NotificationTemplateRepository;
//import com.notification.project.dto.TemplateRequest;
//import com.notification.project.dto.TemplateResponse;
//import com.notification.project.mapper.NotificationMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.reactive.server.WebTestClient;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//
//@SpringBootTest
//@AutoConfigureWebTestClient
//class NotificationTemplateHandlerIntegrationTest {
//
//    @Autowired
//    private WebTestClient webTestClient;
//
//    @Autowired
//    private  NotificationTemplateRepository repository;
//
//    @Autowired
//    private NotificationMapper notificationMapper;
//
//    private Long testUserId = 1L;
//
//    @BeforeEach
//    void cleanDb() {
//        repository.deleteAll().block(); // clear db before each test
//    }
//
//    @Test
//    void createTemplate_shouldReturnCreatedTemplate() {
//
//        TemplateRequest requestDto = new TemplateRequest();
//        requestDto.setTemplateName("WELCOME");
//        requestDto.setSubject("Hello");
//        requestDto.setBody("Welcome to our service!");
//
//        webTestClient.post()
//                .uri("/api/notifications")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(requestDto)
//                .exchange()
//                .expectStatus().isCreated()
//                .expectBody(TemplateResponse.class)
//                .value(response -> {
//                    assertThat(response.getId()).isNotNull();
//                    assertThat(response.getTemplateName()).isEqualTo("WELCOME");
//                });
//    }
//}
//
