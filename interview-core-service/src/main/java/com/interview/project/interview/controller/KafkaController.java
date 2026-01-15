package com.interview.project.interview.controller;

import com.interview.project.interview.dto.CreateProductRestDTO;
import com.interview.project.interview.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {

    @Autowired
    private KafkaService kafkaService;

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody CreateProductRestDTO product){
        String productId = kafkaService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productId);
    }
}
