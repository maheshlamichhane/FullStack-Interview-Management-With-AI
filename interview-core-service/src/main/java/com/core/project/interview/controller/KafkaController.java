package com.core.project.interview.controller;

import com.core.project.interview.dto.CreateProductRestDTO;
import com.core.project.interview.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/user")
public class KafkaController {

    @Autowired
    private KafkaService kafkaService;

    @PostMapping("/kafka")
    public ResponseEntity<String> createProduct(@RequestBody CreateProductRestDTO product) throws ExecutionException, InterruptedException {
        String productId = kafkaService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productId);
    }
}
