package com.itsutra.project.controller;


import com.itsutra.project.dto.DocumentCreateRequestDTO;
import com.itsutra.project.dto.DocumentResponseDTO;
import com.itsutra.project.dto.DocumentUpdateRequestDTO;
import com.itsutra.project.dto.DocumentVersionRequestDTO;
import com.itsutra.project.enums.DocumentCategory;
import com.itsutra.project.service.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping
    public ResponseEntity<DocumentResponseDTO> createDocument(@Valid @RequestBody DocumentCreateRequestDTO request) {
        DocumentResponseDTO response = documentService.createDocument(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<DocumentResponseDTO>> getAllDocuments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<DocumentResponseDTO> documents = documentService.getAllDocuments(pageable);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponseDTO> getDocumentById(@PathVariable Long id) {
        DocumentResponseDTO response = documentService.getDocumentById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentResponseDTO> updateDocument(
            @PathVariable Long id,
            @Valid @RequestBody DocumentUpdateRequestDTO request) {

        DocumentResponseDTO response = documentService.updateDocument(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/versions")
    public ResponseEntity<DocumentResponseDTO> createDocumentVersion(
            @PathVariable Long id,
            @Valid @RequestBody DocumentVersionRequestDTO request) {

        DocumentResponseDTO response = documentService.createDocumentVersion(id, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/versions")
    public ResponseEntity<List<DocumentResponseDTO>> getDocumentVersions(@PathVariable Long id) {
        List<DocumentResponseDTO> versions = documentService.getDocumentVersions(id);
        return ResponseEntity.ok(versions);
    }

    @PostMapping("/{id}/verify")
    public ResponseEntity<DocumentResponseDTO> verifyDocument(
            @PathVariable Long id,
            @RequestParam Long verifiedBy) {

        DocumentResponseDTO response = documentService.verifyDocument(id, verifiedBy);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/tag")
    public ResponseEntity<Page<DocumentResponseDTO>> searchDocumentsByTag(
            @RequestParam String tag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<DocumentResponseDTO> documents = documentService.searchDocumentsByTag(tag, pageable);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<DocumentResponseDTO>> getDocumentsByCategory(
            @PathVariable DocumentCategory category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<DocumentResponseDTO> documents = documentService.getDocumentsByCategory(category, pageable);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDocumentStatistics() {
        Map<String, Object> stats = documentService.getDocumentStatistics();
        return ResponseEntity.ok(stats);
    }
}
