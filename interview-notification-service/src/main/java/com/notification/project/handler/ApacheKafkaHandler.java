package com.notification.project.handler;

import com.common.project.dto.ProductCreatedEventDTO;
import com.notification.project.exception.NonRetryableException;
import com.notification.project.exception.RetryableException;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics="interview_topic")
public class ApacheKafkaHandler {


    @KafkaHandler
    public void handle(@Payload ProductCreatedEventDTO event, @Header("messageId") String messageId, @Header(KafkaHeaders.RECEIVED_KEY) String messageKey) {
        System.out.println("Received a new Request"+event.getTitle());

        if(event.getProductId() == null){
            throw new NonRetryableException("Product Id is null");
        }
        else if(event.getPrice() == null){
            throw new RetryableException("Price is null");
        }

        // you can use the h2 db to store the message key to check if the message is already
        //processed or not.
    }
}
