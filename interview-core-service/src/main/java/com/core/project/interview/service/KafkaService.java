package com.core.project.interview.service;


import com.common.project.dto.ProductCreatedEventDTO;
import com.core.project.interview.dto.CreateProductRestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class KafkaService {

    @Autowired
    private KafkaTemplate<String, ProductCreatedEventDTO> kafkaTemplate;

    public String createProduct(CreateProductRestDTO createProductRestDTO) throws ExecutionException, InterruptedException {
        String productId = UUID.randomUUID().toString();
        ProductCreatedEventDTO productCreatedEventDTO = new ProductCreatedEventDTO();
        productCreatedEventDTO.setProductId(productId);
        productCreatedEventDTO.setTitle(createProductRestDTO.getTitle());
        productCreatedEventDTO.setPrice(createProductRestDTO.getPrice());
        productCreatedEventDTO.setQuantity(createProductRestDTO.getQuantity());

        SendResult<String,ProductCreatedEventDTO> result = kafkaTemplate.send("interview_topic",productId,productCreatedEventDTO).get();


        // async call
//        CompletableFuture<SendResult<String,ProductCreatedEventDTO>> future = kafkaTemplate.send("interview_topic",productId, productCreatedEventDTO);
//        future.whenComplete((result,exception) -> {
//            if (exception != null) {
//                System.out.println("Failed to send message: "+exception.getMessage());
//            }
//            else{
//                System.out.println("Message sent successfully");
//            }
//        });
//        future.join();
        return productId;
    }
}
