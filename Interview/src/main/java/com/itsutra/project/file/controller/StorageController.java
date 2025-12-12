package com.itsutra.project.file.controller;

import com.itsutra.project.file.dto.StorageCreateRequestDTO;
import com.itsutra.project.file.dto.StorageQuotaUpdateRequestDTO;
import com.itsutra.project.file.dto.StorageResponseDTO;
import com.itsutra.project.file.dto.StorageStatsResponseDTO;
import com.itsutra.project.file.service.StorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/interviews/storage")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    @PostMapping("/configurations")
    public ResponseEntity<StorageResponseDTO> createStorageConfiguration(@Valid @RequestBody StorageCreateRequestDTO request) {
        StorageResponseDTO response = storageService.createStorageConfiguration(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/configurations")
    public ResponseEntity<List<StorageResponseDTO>> getAllStorageConfigurations() {
        List<StorageResponseDTO> configurations = storageService.getAllStorageConfigurations();
        return ResponseEntity.ok(configurations);
    }


    @GetMapping("/configurations/{id}")
    public ResponseEntity<StorageResponseDTO> getStorageConfiguration(@PathVariable Long id) {
        StorageResponseDTO response = storageService.getStorageConfiguration(id);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/configurations/{id}")
    public ResponseEntity<StorageResponseDTO> updateStorageConfiguration(
            @PathVariable Long id,
            @Valid @RequestBody StorageCreateRequestDTO request) {

        StorageResponseDTO response = storageService.updateStorageConfiguration(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/configurations/{id}/quota")
    public ResponseEntity<StorageResponseDTO> updateStorageQuota(
            @PathVariable Long id,
            @Valid @RequestBody StorageQuotaUpdateRequestDTO request) {

        StorageResponseDTO response = storageService.updateStorageQuota(id, request);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/stats")
    public ResponseEntity<StorageStatsResponseDTO> getStorageStats() {
        StorageStatsResponseDTO stats = storageService.getStorageStats();
        return ResponseEntity.ok(stats);
    }


    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getStorageHealth() {
        Map<String, Object> health = storageService.getStorageHealth();
        return ResponseEntity.ok(health);
    }

    @DeleteMapping("/configurations/{id}")
    public ResponseEntity<Void> deleteStorageConfiguration(@PathVariable Long id) {
        storageService.deleteStorageConfiguration(id);
        return ResponseEntity.noContent().build();
    }




}
