package com.itsutra.project.controller;

import com.itsutra.project.dto.*;
import com.itsutra.project.enums.FileCategory;
import com.itsutra.project.service.FileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<FileResponseDTO> uploadFile(@Valid @ModelAttribute FileUploadRequestDTO request) {
        FileResponseDTO response = fileService.uploadFile(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<FileResponseDTO>> getAllFiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @ModelAttribute FileSearchRequestDTO searchRequest) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<FileResponseDTO> files = fileService.getAllFiles(searchRequest, pageable);
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
    public ResponseEntity<Page<FileResponseDTO>> getFilesByCategory(
            @PathVariable FileCategory category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<FileResponseDTO> files = fileService.getFilesByCategory(category, pageable);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/stats/storage")
    public ResponseEntity<Map<String, Object>> getStorageStatistics() {
        Map<String, Object> stats = fileService.getStorageStatistics();
        return ResponseEntity.ok(stats);
    }
}
