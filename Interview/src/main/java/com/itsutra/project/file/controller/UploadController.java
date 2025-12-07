package com.itsutra.project.file.controller;


import com.itsutra.project.file.dto.ChunkUploadRequestDTO;
import com.itsutra.project.file.dto.UploadInitRequestDTO;
import com.itsutra.project.file.dto.UploadInitResponseDTO;
import com.itsutra.project.file.dto.UploadStatusResponseDTO;
import com.itsutra.project.file.service.UploadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/uploads")
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;

    @PostMapping("/init")
    public ResponseEntity<UploadInitResponseDTO> initializeUpload(@Valid @RequestBody UploadInitRequestDTO request) {
        UploadInitResponseDTO response = uploadService.initializeUpload(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PostMapping("/chunk")
    public ResponseEntity<UploadStatusResponseDTO> uploadChunk(@Valid @ModelAttribute ChunkUploadRequestDTO request) {
        UploadStatusResponseDTO response = uploadService.uploadChunk(request);
        return ResponseEntity.ok(response);
    }
//
//    @PostMapping("/complete")
//    public ResponseEntity<FileResponseDTO> completeUpload(@Valid @RequestBody UploadCompleteRequestDTO request) {
//        FileResponseDTO response = uploadService.completeUpload(request);
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/{sessionId}/status")
//    public ResponseEntity<UploadStatusResponseDTO> getUploadStatus(@PathVariable String sessionId) {
//        UploadStatusResponseDTO response = uploadService.getUploadStatus(sessionId);
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/{sessionId}/cancel")
//    public ResponseEntity<Void> cancelUpload(@PathVariable String sessionId) {
//        uploadService.cancelUpload(sessionId);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PostMapping("/{sessionId}/resume")
//    public ResponseEntity<UploadInitResponseDTO> resumeUpload(@PathVariable String sessionId) {
//        UploadInitResponseDTO response = uploadService.resumeUpload(sessionId);
//        return ResponseEntity.ok(response);
//    }
}
