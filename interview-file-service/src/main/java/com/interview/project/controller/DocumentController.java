package com.interview.project.controller;//package com.itsutra.project.file.controller;
//
//
//import com.itsutra.project.file.dto.DocumentCreateRequestDTO;
//import com.itsutra.project.file.dto.DocumentResponseDTO;
//import com.itsutra.project.file.dto.DocumentUpdateRequestDTO;
//import com.itsutra.project.file.dto.DocumentVersionRequestDTO;
//import com.itsutra.project.file.enums.DocumentCategory;
//import com.itsutra.project.file.service.DocumentService;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/interviews/documents")
//@RequiredArgsConstructor
//public class DocumentController {
//
//    private final DocumentService documentService;
//
//    @PostMapping
//    public ResponseEntity<DocumentResponseDTO> createDocument(@Valid @RequestBody DocumentCreateRequestDTO request) {
//        DocumentResponseDTO response = documentService.createDocument(request);
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<DocumentResponseDTO>> getAllDocuments() {
//        List<DocumentResponseDTO> documents = documentService.getAllDocuments();
//        return ResponseEntity.ok(documents);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<DocumentResponseDTO> getDocumentById(@PathVariable Long id, HttpServletRequest request) {
//        DocumentResponseDTO response = documentService.getDocumentById(id,request);
//        return ResponseEntity.ok(response);
//    }
//
//
//
//    @PutMapping("/{id}")
//    public ResponseEntity<DocumentResponseDTO> updateDocument(
//            @PathVariable Long id,
//            @Valid @RequestBody DocumentUpdateRequestDTO request, HttpServletRequest httpServletRequest) {
//
//        DocumentResponseDTO response = documentService.updateDocument(id, request,httpServletRequest);
//        return ResponseEntity.ok(response);
//    }
//
//
//    @PostMapping("/{id}/versions")
//    public ResponseEntity<DocumentResponseDTO> createDocumentVersion(
//            @PathVariable Long id,
//            @Valid @RequestBody DocumentVersionRequestDTO request) {
//
//        DocumentResponseDTO response = documentService.createDocumentVersion(id, request);
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
//    }
//
//
//
//    @GetMapping("/{id}/versions")
//    public ResponseEntity<List<DocumentResponseDTO>> getDocumentVersions(@PathVariable Long id) {
//        List<DocumentResponseDTO> versions = documentService.getDocumentVersions(id);
//        return ResponseEntity.ok(versions);
//    }
//
//
//
//    @PostMapping("/{id}/verify")
//    public ResponseEntity<DocumentResponseDTO> verifyDocument(
//            @PathVariable Long id) {
//
//        DocumentResponseDTO response = documentService.verifyDocument(id);
//        return ResponseEntity.ok(response);
//    }
//
//
//
//    @GetMapping("/search/tag")
//    public ResponseEntity<List<DocumentResponseDTO>> searchDocumentsByTag(
//            @RequestParam String tag) {
//        List<DocumentResponseDTO> documents = documentService.searchDocumentsByTag(tag);
//        return ResponseEntity.ok(documents);
//    }
//
//
//
//    @GetMapping("/category/{category}")
//    public ResponseEntity<List<DocumentResponseDTO>> getDocumentsByCategory(
//            @PathVariable DocumentCategory category) {
//        List<DocumentResponseDTO> documents = documentService.getDocumentsByCategory(category);
//        return ResponseEntity.ok(documents);
//    }
//
//
//    @GetMapping("/stats")
//    public ResponseEntity<Map<String, Object>> getDocumentStatistics() {
//        Map<String, Object> stats = documentService.getDocumentStatistics();
//        return ResponseEntity.ok(stats);
//    }
//
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
//        documentService.deleteDocument(id);
//        return ResponseEntity.noContent().build();
//    }
//
//}
