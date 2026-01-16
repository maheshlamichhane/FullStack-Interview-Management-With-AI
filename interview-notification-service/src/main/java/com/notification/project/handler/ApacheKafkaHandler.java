package com.notification.project.handler;

import com.common.project.dto.ProductCreatedEventDTO;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics="interview_topic")
public class ApacheKafkaHandler {


    @KafkaHandler
    public void handle(ProductCreatedEventDTO event) {
        System.out.println("Received a new Request"+event.getTitle());
    }
}
