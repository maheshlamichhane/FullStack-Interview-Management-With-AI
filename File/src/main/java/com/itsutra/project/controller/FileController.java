package com.itsutra.project.controller;

import com.itsutra.project.dto.*;
import com.itsutra.project.enums.FileCategory;
import com.itsutra.project.service.FileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<FileResponseDTO> uploadFile(@Valid @ModelAttribute FileUploadRequestDTO request) {
        FileResponseDTO response = fileService.uploadFile(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FileResponseDTO>> getAllFiles(
            @ModelAttribute FileSearchRequestDTO searchRequest) {
        List<FileResponseDTO> files = fileService.getAllFiles(searchRequest);
        return ResponseEntity.ok(files);
    }


    @GetMapping("/{id}")
    public ResponseEntity<FileResponseDTO> getFileById(@PathVariable Long id) {

        FileResponseDTO response = fileService.getFileById(id);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/storage-key/{storageKey}")
    public ResponseEntity<FileResponseDTO> getFileByStorageKey(@PathVariable String storageKey) {

        FileResponseDTO response = fileService.getFileByStorageKey(storageKey);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FileResponseDTO> updateFile(
            @PathVariable Long id,
            @Valid @RequestBody FileUpdateRequestDTO request) {

        FileResponseDTO response = fileService.updateFile(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}/download")
    public ResponseEntity<FileDownloadResponseDTO> downloadFile(@PathVariable Long id) {
        FileDownloadResponseDTO response = fileService.downloadFile(id);

        return ResponseEntity.ok(response);
    }



    @GetMapping("/category/{category}")
    public ResponseEntity<List<FileResponseDTO>> getFilesByCategory(
            @PathVariable FileCategory category) {
        List<FileResponseDTO> files = fileService.getFilesByCategory(category);
        return ResponseEntity.ok(files);
    }



    @GetMapping("/stats/storage")
    public ResponseEntity<Map<String, Object>> getStorageStatistics() {
        Map<String, Object> stats = fileService.getStorageStatistics();
        return ResponseEntity.ok(stats);
    }
}
