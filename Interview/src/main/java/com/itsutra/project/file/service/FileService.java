package com.itsutra.project.file.service;


import com.itsutra.project.common.dao.UserDAO;
import com.itsutra.project.common.entity.User;
import com.itsutra.project.common.service.AuthenticationService;
import com.itsutra.project.file.dao.FileDAO;
import com.itsutra.project.file.dto.*;
import com.itsutra.project.file.entity.File;
import com.itsutra.project.file.enums.FileCategory;
import com.itsutra.project.file.enums.FileStatus;
import com.itsutra.project.file.mapper.FileStorageMapper;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final FileDAO fileDAO;
    private final UserDAO userDAO;
    private final FileStorageMapper fileStorageMapper;
    private final StorageService storageService;
    private final FileProcessingService fileProcessingService;
    private final AuthenticationService authenticationService;

    @Transactional
    public FileResponseDTO uploadFile(FileUploadRequestDTO request) {
        log.info("Uploading file: {}", request.getFile().getOriginalFilename());

        try {
            // Validate file
            validateFile(request.getFile());

            // Generate unique storage key
            String storageKey = generateStorageKey(request.getFile().getOriginalFilename());

            // Get user who uploaded the file
            User uploadedBy = null;
            if (request.getUploadedById() != null) {
                uploadedBy = userDAO.findById(request.getUploadedById())
                        .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + request.getUploadedById()));
            }

            // Create file entity
            File file = fileStorageMapper.toFileEntity(request, storageKey, uploadedBy);

            // Store file in storage
            String storagePath = storageService.storeFile(request.getFile(), storageKey, file.getCategory());
            file.setStoragePath(storagePath);


            User user = authenticationService.getCurrentUser();
            file.setCreatedBy(user);
            // Save file metadata
            File savedFile = fileDAO.save(file);


            fileProcessingService.processFileAsync(savedFile);

            // Generate download URL
            String downloadUrl = storageService.generateDownloadUrl(savedFile.getStorageKey());
            String previewUrl = fileProcessingService.generatePreviewUrl(savedFile);

            log.info("Successfully uploaded file with id: {}", savedFile.getId());
            return fileStorageMapper.toFileResponse(savedFile, downloadUrl, previewUrl);

        } catch (Exception e) {
            log.error("Error uploading file: {}", request.getFile().getOriginalFilename(), e);
            throw new RuntimeException("File upload failed: " + e.getMessage(), e);
        }
    }


    @Transactional(readOnly = true)
    public List<FileResponseDTO> getAllFiles(FileSearchRequestDTO searchRequest) {
        log.debug("Fetching all files with filters");

        User currentUser = authenticationService.getCurrentUser();

        // Build specification with createdBy filter
        Specification<File> spec = buildFileSearchSpecification(searchRequest)
                .and((root, query, cb) ->
                        cb.equal(root.get("createdBy").get("id"), currentUser.getId())
                );

        // Find all files with specification
        List<File> files = fileDAO.findAll(spec);

        return files.stream().map(file -> {
            String downloadUrl = storageService.generateDownloadUrl(file.getStorageKey());
            String previewUrl = fileProcessingService.generatePreviewUrl(file);
            return fileStorageMapper.toFileResponse(file, downloadUrl, previewUrl);
        }).toList();
    }



    @Transactional(readOnly = true)
    public FileResponseDTO getFileById(Long id) {
        log.debug("Fetching file by id: {}", id);
        User currentUser = authenticationService.getCurrentUser();
        File file = fileDAO.findByIdAndCreatedById(id,currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("File not found with id: " + id));

        // Increment access count
        file.incrementAccessCount();
        fileDAO.save(file);

        String downloadUrl = storageService.generateDownloadUrl(file.getStorageKey());
        String previewUrl = fileProcessingService.generatePreviewUrl(file);

        return fileStorageMapper.toFileResponse(file, downloadUrl, previewUrl);
    }


    @Transactional(readOnly = true)
    public FileResponseDTO getFileByStorageKey(String storageKey) {
        log.debug("Fetching file by storage key: {}", storageKey);
        User currentUser = authenticationService.getCurrentUser();
        File file = fileDAO.findByCreatedByIdAndStorageKey(currentUser.getId(),storageKey)
                .orElseThrow(() -> new IllegalArgumentException("File not found with storage key: " + storageKey));

        file.incrementAccessCount();
        fileDAO.save(file);

        String downloadUrl = storageService.generateDownloadUrl(file.getStorageKey());
        String previewUrl = fileProcessingService.generatePreviewUrl(file);

        return fileStorageMapper.toFileResponse(file, downloadUrl, previewUrl);
    }



    @Transactional
    public FileResponseDTO updateFile(Long id, FileUpdateRequestDTO request) {
        log.info("Updating file with id: {}", id);

        User currentUser = authenticationService.getCurrentUser();
        File file = fileDAO.findByIdAndCreatedById(id,currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("File not found with id: " + id));

        Optional.ofNullable(request.getName()).ifPresent(file::setName);
        Optional.ofNullable(request.getDescription()).ifPresent(file::setDescription);
        Optional.ofNullable(request.getCategory()).ifPresent(file::setCategory);
        Optional.ofNullable(request.getRetentionPeriod()).ifPresent(file::setRetentionPeriod);

        if (request.getRetentionPeriod() != null) {
            file.setExpiresAt(LocalDateTime.now().plusDays(request.getRetentionPeriod()));
        }

        File updatedFile = fileDAO.save(file);
        String downloadUrl = storageService.generateDownloadUrl(updatedFile.getStorageKey());
        String previewUrl = fileProcessingService.generatePreviewUrl(updatedFile);

        log.info("Successfully updated file with id: {}", id);
        return fileStorageMapper.toFileResponse(updatedFile, downloadUrl, previewUrl);
    }


    @Transactional
    public void deleteFile(Long id) {
        log.info("Deleting file with id: {}", id);

        User currentUser = authenticationService.getCurrentUser();
        File file = fileDAO.findByIdAndCreatedById(id,currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("File not found with id: " + id));

        // Delete from storage
        storageService.deleteFile(file.getStorageKey());

        // Soft delete - mark as deleted
        file.setStatus(FileStatus.DELETED);
        fileDAO.save(file);

        log.info("Successfully deleted file with id: {}", id);
    }


    @Transactional
    public FileDownloadResponseDTO downloadFile(Long id) {
        log.info("Downloading file with id: {}", id);

        User currentUser = authenticationService.getCurrentUser();
        File file = fileDAO.findByIdAndCreatedById(id,currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("File not found with id: " + id));

        // Increment access count
        file.incrementAccessCount();
        fileDAO.save(file);

        // Generate signed download URL
        String downloadUrl = storageService.generateDownloadUrl(file.getStorageKey());
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(24);

        FileDownloadResponseDTO response = new FileDownloadResponseDTO();
        response.setDownloadUrl(downloadUrl);
        response.setFileName(file.getOriginalName());
        response.setMimeType(file.getMimeType());
        response.setFileSize(file.getSize());
        response.setExpiresAt(expiresAt);
        response.setRequiresAuthentication(true);

        log.info("Generated download URL for file id: {}", id);
        return response;
    }


    @Transactional(readOnly = true)
    public List<FileResponseDTO> getFilesByCategory(FileCategory category) {
        log.debug("Fetching files by category: {}", category);
        User currentUser = authenticationService.getCurrentUser();
        List<File> files = fileDAO.findByCategoryAndCreatedById(category,currentUser.getId());

        return files.stream().map(file -> {
            String downloadUrl = storageService.generateDownloadUrl(file.getStorageKey());
            String previewUrl = fileProcessingService.generatePreviewUrl(file);
            return fileStorageMapper.toFileResponse(file, downloadUrl, previewUrl);
        }).toList();
    }


    @Transactional(readOnly = true)
    public Map<String, Object> getStorageStatistics() {
        log.debug("Fetching storage statistics");

        Long totalStorageUsed = fileDAO.getTotalStorageUsed();
        List<Object[]> categoryUsage = fileDAO.getStorageUsageByCategory();
        List<Object[]> providerDistribution = fileDAO.getStorageDistribution();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalStorageUsed", totalStorageUsed);
        stats.put("categoryUsage", categoryUsage.stream()
                .collect(java.util.stream.Collectors.toMap(
                        arr -> arr[0].toString(),
                        arr -> Map.of(
                                "fileCount", arr[1],
                                "storageUsed", arr[2]
                        )
                )));
        stats.put("providerDistribution", providerDistribution.stream()
                .collect(java.util.stream.Collectors.toMap(
                        arr -> arr[0].toString(),
                        arr -> Map.of(
                                "fileCount", arr[1],
                                "storageUsed", arr[2]
                        )
                )));

        return stats;
    }
//
//    // Scheduled task to clean up expired files
//    @Scheduled(cron = "0 0 2 * * ?") // Run daily at 2 AM
//    public void cleanupExpiredFiles() {
//        log.info("Starting expired files cleanup");
//
//        List<File> expiredFiles = fileDAO.findExpiredFiles(LocalDateTime.now());
//
//        for (File file : expiredFiles) {
//            try {
//                log.info("Deleting expired file: {} (id: {})", file.getName(), file.getId());
//                deleteFile(file.getId());
//            } catch (Exception e) {
//                log.error("Error deleting expired file: {}", file.getId(), e);
//            }
//        }
//
//        log.info("Expired files cleanup completed. Deleted {} files.", expiredFiles.size());
//    }
//
//    // Helper methods
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        if (file.getSize() > 100 * 1024 * 1024) { // 100MB limit
            throw new IllegalArgumentException("File size exceeds maximum limit of 100MB");
        }

        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null || !isAllowedContentType(contentType)) {
            throw new IllegalArgumentException("File type not allowed: " + contentType);
        }
    }

    private boolean isAllowedContentType(String contentType) {
        // Define allowed content types
        List<String> allowedTypes = List.of(
                "image/jpeg", "image/png", "image/gif", "image/webp",
                "application/pdf", "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "application/vnd.ms-excel",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "text/plain", "video/mp4", "audio/mpeg"
        );
        return allowedTypes.contains(contentType);
    }

    private String generateStorageKey(String originalFileName) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String randomId = UUID.randomUUID().toString().substring(0, 8);
        String extension = originalFileName != null ?
                originalFileName.substring(originalFileName.lastIndexOf('.')) : "";
        return timestamp + "_" + randomId + extension;
    }

    private Specification<File> buildFileSearchSpecification(FileSearchRequestDTO searchRequest) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchRequest.getName() != null) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + searchRequest.getName().toLowerCase() + "%"
                ));
            }

            if (searchRequest.getCategory() != null) {
                predicates.add(criteriaBuilder.equal(root.get("category"), searchRequest.getCategory()));
            }

            if (searchRequest.getMimeType() != null) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("mimeType")),
                        "%" + searchRequest.getMimeType().toLowerCase() + "%"
                ));
            }

            if (searchRequest.getMinSize() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("size"), searchRequest.getMinSize()));
            }

            if (searchRequest.getMaxSize() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("size"), searchRequest.getMaxSize()));
            }

            if (searchRequest.getUploadedAfter() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), searchRequest.getUploadedAfter()));
            }

            if (searchRequest.getUploadedBefore() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), searchRequest.getUploadedBefore()));
            }

            if (searchRequest.getUploadedById() != null) {
                predicates.add(criteriaBuilder.equal(root.get("uploadedBy").get("id"), searchRequest.getUploadedById()));
            }

            // Only show active files
            predicates.add(criteriaBuilder.equal(root.get("status"), FileStatus.ACTIVE));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}