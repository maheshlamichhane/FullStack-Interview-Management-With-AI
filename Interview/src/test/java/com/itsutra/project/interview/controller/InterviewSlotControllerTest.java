package com.itsutra.project.interview.controller;

import com.itsutra.project.interview.dto.InterviewSlotRequest;
import com.itsutra.project.interview.dto.InterviewSlotResponse;
import com.itsutra.project.interview.enums.SlotStatus;
import com.itsutra.project.interview.service.InterviewSlotService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebFluxTest(InterviewSlotController.class)
@Import(InterviewSlotControllerTest.MockConfig.class)
class InterviewSlotControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private InterviewSlotService slotService;

    @Autowired
    private StreamBridge streamBridge;

    private final Long interviewerId = 567284L;

    @Test
    void createSlot_shouldReturn201() {
        InterviewSlotRequest request = new InterviewSlotRequest();
//        request.setStartTime(LocalDateTime.of(2026, 1, 9, 6, 20));
//        request.setEndTime(LocalDateTime.of(2026, 12, 30, 7, 30));
        request.setStatus(SlotStatus.AVAILABLE);
        InterviewSlotResponse response = new InterviewSlotResponse();

        Mockito.when(slotService.createSlot(any(), eq(interviewerId)))
                .thenReturn(Mono.just(response));

        Mockito.when(streamBridge.send(any(), any()))
                .thenReturn(true);

        webTestClient.post()
                .uri("/api/interviews/interview-slots")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(InterviewSlotResponse.class);
    }

    /* ---------------- GET ALL SLOTS ---------------- */

    @Test
    void getAllSlots_shouldReturnFlux() {
        Mockito.when(slotService.getAllSlots())
                .thenReturn(Flux.just(new InterviewSlotResponse()));

        webTestClient.get()
                .uri("/api/interviews/interview-slots")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(InterviewSlotResponse.class)
                .hasSize(1);
    }

    /* ---------------- GET AVAILABLE SLOTS ---------------- */

    @Test
    void getAvailableSlots_shouldReturnFlux() {
        Mockito.when(slotService.getAvailableSlots())
                .thenReturn(Flux.just(new InterviewSlotResponse()));

        webTestClient.get()
                .uri("/api/interviews/interview-slots/available")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(InterviewSlotResponse.class)
                .hasSize(1);
    }

    /* ---------------- GET BY INTERVIEW ID ---------------- */

    @Test
    void getSlotsByInterview_shouldReturnFlux() {
        Mockito.when(slotService.getSlotsByInterviewId(1, interviewerId))
                .thenReturn(Flux.just(new InterviewSlotResponse()));

        webTestClient.get()
                .uri("/api/interviews/interview-slots/interview/1")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(InterviewSlotResponse.class)
                .hasSize(1);
    }

    /* ---------------- CANCEL SLOT ---------------- */

    @Test
    void cancelSlot_shouldReturnMono() {
        Mockito.when(slotService.cancelSlot(1, interviewerId, 100L, "Not available"))
                .thenReturn(Mono.just(new InterviewSlotResponse()));

        webTestClient.post()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/interviews/interview-slots/1/cancel")
                                .queryParam("cancelledBy", 100)
                                .queryParam("reason", "Not available")
                                .build()
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody(InterviewSlotResponse.class);
    }

    /* ---------- Test Configuration ---------- */
    @TestConfiguration
    static class MockConfig {

        @Bean
        InterviewSlotService slotService() {
            return Mockito.mock(InterviewSlotService.class);
        }

        @Bean
        StreamBridge streamBridge() {
            return Mockito.mock(StreamBridge.class);
        }
    }
}


