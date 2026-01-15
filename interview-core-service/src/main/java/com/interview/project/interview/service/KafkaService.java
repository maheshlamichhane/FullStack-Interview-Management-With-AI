package com.interview.project.interview.service;

import com.interview.project.interview.dto.CreateProductRestDTO;
import com.interview.project.interview.dto.ProductCreatedEventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class KafkaService {

    @Autowired
    private KafkaTemplate<String, ProductCreatedEventDTO> kafkaTemplate;

    public String createProduct(CreateProductRestDTO createProductRestDTO) {
        String productId = UUID.randomUUID().toString();
        ProductCreatedEventDTO productCreatedEventDTO = new ProductCreatedEventDTO();
        productCreatedEventDTO.setProductId(productId);
        productCreatedEventDTO.setTitle(createProductRestDTO.getTitle());
        productCreatedEventDTO.setPrice(createProductRestDTO.getPrice());
        productCreatedEventDTO.setQuantity(createProductRestDTO.getQuantity());

        CompletableFuture<SendResult<String,ProductCreatedEventDTO>> future = kafkaTemplate.send("interview_topic", productCreatedEventDTO);
        future.whenComplete((result,exception) -> {
            if (exception != null) {
                System.out.println("Failed to send message: "+exception.getMessage());
            }
            else{
                System.out.println("Message sent successfully");
            }
        });
        future.join();
        return productId;
    }
}
