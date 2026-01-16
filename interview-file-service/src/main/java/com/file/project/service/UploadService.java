package com.file.project.service;//package com.itsutra.project.file.service;
//
//import com.itsutra.project.common.dao.UserDAO;
//import com.itsutra.project.common.entity.User;
//import com.itsutra.project.common.service.AuthenticationService;
//import com.itsutra.project.file.dao.FileDAO;
//import com.itsutra.project.file.dao.UploadSessionDAO;
//import com.itsutra.project.file.dto.ChunkUploadRequestDTO;
//import com.itsutra.project.file.dto.UploadInitRequestDTO;
//import com.itsutra.project.file.dto.UploadInitResponseDTO;
//import com.itsutra.project.file.dto.UploadStatusResponseDTO;
//import com.itsutra.project.file.entity.File;
//import com.itsutra.project.file.entity.UploadSession;
//import com.itsutra.project.file.enums.FileCategory;
//import com.itsutra.project.file.enums.FileStatus;
//import com.itsutra.project.file.enums.UploadStatus;
//import com.itsutra.project.file.mapper.FileStorageMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//@Slf4j
//public class UploadService {
//
//    private final UploadSessionDAO uploadSessionDAO;
//    private final FileDAO fileDAO;
//    private final UserDAO userDAO;
//    private final FileStorageMapper fileStorageMapper;
//    private final StorageService storageService;
//    private final AuthenticationService authenticationService;
//
//    @Transactional
//    public UploadInitResponseDTO initializeUpload(UploadInitRequestDTO request) {
//        log.info("Initializing upload for file: {}", request.getFileName());
//
//        // Generate unique IDs
//        String sessionId = UUID.randomUUID().toString();
//        String uploadId = UUID.randomUUID().toString();
//
//        // Create file entity (in UPLOADING status)
//        File file = File.builder()
//                .name(request.getFileName())
//                .originalName(request.getFileName())
//                .storageKey(generateStorageKey(request.getFileName()))
//                .mimeType(request.getMimeType())
//                .size(request.getFileSize())
//                .extension(extractExtension(request.getFileName()))
//                .category(request.getCategory() != null ? request.getCategory() : FileCategory.OTHER)
//                .status(FileStatus.UPLOADING)
//                .build();
//
//        User currentUser = authenticationService.getCurrentUser();
//        file.setCreatedBy(currentUser);
//        File savedFile = fileDAO.save(file);
//
//        // Create upload session
//        UploadSession uploadSession = fileStorageMapper.toUploadSessionEntity(request, savedFile, sessionId, uploadId);
//        UploadSession savedSession = uploadSessionDAO.save(uploadSession);
//
//        // Generate upload URLs for chunks (if chunked upload)
//        Map<String, String> uploadUrls = new HashMap<>();
//        if (request.getChunkSize() != null && request.getChunkSize() > 0) {
//            for (int i = 0; i < uploadSession.getTotalChunks(); i++) {
//                String chunkUrl = storageService.generateChunkUploadUrl(
//                        savedSession.getSessionId(), i, request.getChunkSize());
//                uploadUrls.put(String.valueOf(i), chunkUrl);
//            }
//        } else {
//            // Single file upload URL
//            String uploadUrl = storageService.generateUploadUrl(savedSession.getSessionId());
//            uploadUrls.put("single", uploadUrl);
//        }
//
//        log.info("Upload initialized with session id: {}", sessionId);
//        return fileStorageMapper.toUploadInitResponse(savedSession, uploadUrls);
//    }
//
//    @Transactional
//    public UploadStatusResponseDTO uploadChunk(ChunkUploadRequestDTO request) {
//        log.debug("Uploading chunk {} for session: {}", request.getChunkNumber(), request.getSessionId());
//
//        UploadSession session = uploadSessionDAO.findBySessionId(request.getSessionId())
//                .orElseThrow(() -> new IllegalArgumentException("Upload session not found: " + request.getSessionId()));
//
//        if (session.isExpired()) {
//            throw new IllegalStateException("Upload session has expired");
//        }
//
//        if (!session.canResume()) {
//            throw new IllegalStateException("Upload session cannot be resumed");
//        }
//
//        try {
//            // Store chunk
//            storageService.storeChunk(
//                    session.getSessionId(),
//                    request.getChunkNumber(),
//                    request.getChunk(),
//                    request.getChecksum()
//            );
//
//            // Update session progress
//            session.setUploadedChunks(session.getUploadedChunks() + 1);
//            session.setUploadedSize(session.getUploadedSize() + request.getChunk().getSize());
//            session.setStatus(UploadStatus.IN_PROGRESS);
//
//            UploadSession updatedSession = uploadSessionDAO.save(session);
//            log.debug("Chunk {} uploaded successfully for session: {}", request.getChunkNumber(), request.getSessionId());
//            return fileStorageMapper.toUploadStatusResponse(updatedSession);
//
//        } catch (Exception e) {
//            log.error("Error uploading chunk for session: {}", request.getSessionId(), e);
//            session.setStatus(UploadStatus.FAILED);
//            session.setErrorMessage(e.getMessage());
//            uploadSessionDAO.save(session);
//            throw new RuntimeException("Chunk upload failed: " + e.getMessage(), e);
//        }
//    }
////
////    // Complete Upload
////    public FileResponseDTO completeUpload(UploadCompleteRequestDTO request) {
////        log.info("Completing upload for session: {}", request.getSessionId());
////
////        UploadSession session = uploadSessionDAO.findBySessionId(request.getSessionId())
////                .orElseThrow(() -> new IllegalArgumentException("Upload session not found: " + request.getSessionId()));
////
////        if (session.isExpired()) {
////            throw new IllegalStateException("Upload session has expired");
////        }
////
////        try {
////            // Combine chunks and create final file
////            String storagePath = storageService.combineChunks(
////                    session.getSessionId(),
////                    session.getTotalChunks(),
////                    request.getChecksum()
////            );
////
////            // Update file status and metadata
////            File file = session.getFile();
////            file.setStoragePath(storagePath);
////            file.setStatus(FileStatus.ACTIVE);
////            file.setChecksum(request.getChecksum());
////
////            if (request.getFinalMetadata() != null) {
////                // Update file metadata if needed
////            }
////
////            File savedFile = fileDAO.save(file);
////
////            // Update session
////            session.setStatus(UploadStatus.COMPLETED);
////            session.setCompletedAt(LocalDateTime.now());
////            uploadSessionDAO.save(session);
////
////            // Process file
////            // fileProcessingService.processFileAsync(savedFile);
////
////            // Generate URLs
////            String downloadUrl = storageService.generateDownloadUrl(savedFile.getStorageKey());
////            String previewUrl = ""; // fileProcessingService.generatePreviewUrl(savedFile);
////
////            log.info("Upload completed successfully for session: {}", request.getSessionId());
////            return fileStorageMapper.toFileResponse(savedFile, downloadUrl, previewUrl);
////
////        } catch (Exception e) {
////            log.error("Error completing upload for session: {}", request.getSessionId(), e);
////            session.setStatus(UploadStatus.FAILED);
////            session.setErrorMessage(e.getMessage());
////            uploadSessionDAO.save(session);
////            throw new RuntimeException("Upload completion failed: " + e.getMessage(), e);
////        }
////    }
////
////    // Get Upload Status
////    @Transactional(readOnly = true)
////    public UploadStatusResponseDTO getUploadStatus(String sessionId) {
////        log.debug("Getting upload status for session: {}", sessionId);
////        UploadSession session = uploadSessionDAO.findBySessionId(sessionId)
////                .orElseThrow(() -> new IllegalArgumentException("Upload session not found: " + sessionId));
////        return fileStorageMapper.toUploadStatusResponse(session);
////    }
////
////    // Cancel Upload
////    public void cancelUpload(String sessionId) {
////        log.info("Cancelling upload session: {}", sessionId);
////        UploadSession session = uploadSessionDAO.findBySessionId(sessionId)
////                .orElseThrow(() -> new IllegalArgumentException("Upload session not found: " + sessionId));
////
////        session.setStatus(UploadStatus.CANCELLED);
////        uploadSessionDAO.save(session);
////
////        // Clean up stored chunks
////        storageService.cleanupChunks(sessionId);
////
////        log.info("Upload session cancelled: {}", sessionId);
////    }
////
////    // Resume Upload
////    public UploadInitResponseDTO resumeUpload(String sessionId) {
////        log.info("Resuming upload session: {}", sessionId);
////        UploadSession session = uploadSessionDAO.findBySessionId(sessionId)
////                .orElseThrow(() -> new IllegalArgumentException("Upload session not found: " + sessionId));
////
////        if (!session.canResume()) {
////            throw new IllegalStateException("Upload session cannot be resumed");
////        }
////
////        // Generate new upload URLs for remaining chunks
////        Map<String, String> uploadUrls = new HashMap<>();
////        for (int i = session.getUploadedChunks(); i < session.getTotalChunks(); i++) {
////            String chunkUrl = storageService.generateChunkUploadUrl(
////                    session.getSessionId(), i, session.getChunkSize());
////            uploadUrls.put(String.valueOf(i), chunkUrl);
////        }
////
////        session.setStatus(UploadStatus.IN_PROGRESS);
////        uploadSessionDAO.save(session);
////
////        log.info("Upload session resumed: {}", sessionId);
////        return fileStorageMapper.toUploadInitResponse(session, uploadUrls);
////    }
////
////    // Scheduled task to clean up expired upload sessions
////    @Scheduled(cron = "0 */30 * * * ?") // Run every 30 minutes
////    public void cleanupExpiredSessions() {
////        log.info("Starting expired upload sessions cleanup");
////
////        List<UploadSession> expiredSessions = uploadSessionDAO.findExpiredSessions(LocalDateTime.now());
////
////        for (UploadSession session : expiredSessions) {
////            try {
////                log.info("Cleaning up expired upload session: {}", session.getSessionId());
////                session.setStatus(UploadStatus.EXPIRED);
////                uploadSessionDAO.save(session);
////
////                // Clean up stored chunks
////                storageService.cleanupChunks(session.getSessionId());
////
////                // Delete the associated file if it's still in UPLOADING status
////                if (session.getFile().getStatus() == FileStatus.UPLOADING) {
////                    fileDAO.delete(session.getFile());
////                }
////            } catch (Exception e) {
////                log.error("Error cleaning up expired upload session: {}", session.getSessionId(), e);
////            }
////        }
////
////        log.info("Expired upload sessions cleanup completed. Cleaned up {} sessions.", expiredSessions.size());
////    }
//
//    // Helper methods
//    private String generateStorageKey(String fileName) {
//        String timestamp = String.valueOf(System.currentTimeMillis());
//        String randomId = UUID.randomUUID().toString().substring(0, 8);
//        String extension = fileName != null ?
//                fileName.substring(fileName.lastIndexOf('.')) : "";
//        return "upload_" + timestamp + "_" + randomId + extension;
//    }
//
//    private String extractExtension(String fileName) {
//        if (fileName == null) return null;
//        int lastDotIndex = fileName.lastIndexOf('.');
//        return lastDotIndex > 0 ? fileName.substring(lastDotIndex + 1).toLowerCase() : null;
//    }
//}
