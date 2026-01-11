package com.interview.project.file.mapper;//package com.itsutra.project.file.mapper;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.itsutra.project.common.entity.User;
//import com.itsutra.project.file.dto.*;
//import com.itsutra.project.file.entity.*;
//import com.itsutra.project.file.enums.*;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class FileStorageMapper {
//
//    private final ObjectMapper objectMapper;
//
//    // File Mappings
//    public File toFileEntity(FileUploadRequestDTO request, String storageKey, User uploadedBy) {
//        return File.builder()
//                .name(request.getFile().getOriginalFilename())
//                .originalName(request.getFile().getOriginalFilename())
//                .storageKey(storageKey)
//                .description(request.getDescription())
//                .mimeType(request.getFile().getContentType())
//                .size(request.getFile().getSize())
//                .extension(extractExtension(request.getFile().getOriginalFilename()))
//                .category(request.getCategory() != null ? request.getCategory() : FileCategory.OTHER)
//                .status(FileStatus.ACTIVE)
//                .storageProvider(StorageProvider.LOCAL)
//                .isEncrypted(request.getIsEncrypted() != null ? request.getIsEncrypted() : false)
//                .retentionPeriod(request.getRetentionPeriod())
//                .expiresAt(calculateExpiryDate(request.getRetentionPeriod()))
//                .uploadedBy(uploadedBy)
//                .accessCount(0L)
//                .build();
//    }
//
//    public FileResponseDTO toFileResponse(File entity, String downloadUrl, String previewUrl) {
//        FileResponseDTO response = new FileResponseDTO();
//        response.setId(entity.getId());
//        response.setName(entity.getName());
//        response.setOriginalName(entity.getOriginalName());
//        response.setStorageKey(entity.getStorageKey());
//        response.setDescription(entity.getDescription());
//        response.setMimeType(entity.getMimeType());
//        response.setSize(entity.getSize());
//        response.setFormattedSize(entity.getFormattedSize());
//        response.setExtension(entity.getExtension());
//        response.setCategory(entity.getCategory());
//        response.setStatus(entity.getStatus());
//        response.setStorageProvider(entity.getStorageProvider());
//        response.setStoragePath(entity.getStoragePath());
//        response.setVersion(entity.getVersion());
//        response.setIsEncrypted(entity.getIsEncrypted());
//        response.setRetentionPeriod(entity.getRetentionPeriod());
//        response.setExpiresAt(entity.getExpiresAt());
//        response.setAccessedAt(entity.getAccessedAt());
//        response.setAccessCount(entity.getAccessCount());
//        response.setCreatedAt(entity.getCreatedAt());
//        response.setUpdatedAt(entity.getUpdatedAt());
//        response.setDownloadUrl(downloadUrl);
//        response.setPreviewUrl(previewUrl);
//        response.setIsExpired(entity.isExpired());
//
//        if (entity.getUploadedBy() != null) {
//            UserInfoDTO userInfo = new UserInfoDTO();
//            userInfo.setId(entity.getUploadedBy().getId());
//            userInfo.setName(entity.getUploadedBy().getFullName());
//            userInfo.setEmail(entity.getUploadedBy().getEmail());
//            response.setUploadedBy(userInfo);
//        }
//
//        return response;
//    }
//
//    // Document Mappings
//    public Document toDocumentEntity(DocumentCreateRequestDTO request, File file, User createdBy) {
//        return Document.builder()
//                .title(request.getTitle())
//                .description(request.getDescription())
//                .documentType(request.getDocumentType())
//                .category(request.getCategory())
//                .tags(convertListToString(request.getTags()))
//                .metadata(convertToJson(request.getMetadata()))
//                .isConfidential(request.getIsConfidential() != null ? request.getIsConfidential() : false)
//                .isVerified(false)
//                .version(1)
//                .status(DocumentStatus.APPROVED)
//                .file(file)
//                .createdBy(createdBy)
//                .build();
//    }
//
//    public DocumentResponseDTO toDocumentResponse(Document entity) {
//        DocumentResponseDTO response = new DocumentResponseDTO();
//        response.setId(entity.getId());
//        response.setTitle(entity.getTitle());
//        response.setDescription(entity.getDescription());
//        response.setDocumentType(entity.getDocumentType());
//        response.setCategory(entity.getCategory());
//        response.setTags(convertStringToList(entity.getTags()));
//        response.setMetadata(convertFromJson(entity.getMetadata(), Map.class));
//        response.setIsConfidential(entity.getIsConfidential());
//        response.setIsVerified(entity.getIsVerified());
//        response.setVerifiedBy(entity.getVerifiedBy());
//        response.setVerifiedAt(entity.getVerifiedAt());
//        response.setVersion(entity.getVersion());
//        response.setParentDocumentId(entity.getParentDocumentId());
//        response.setStatus(entity.getStatus());
//        response.setCreatedAt(entity.getCreatedAt());
//        response.setUpdatedAt(entity.getUpdatedAt());
//
//        if (entity.getFile() != null) {
//            response.setFile(toFileResponse(entity.getFile(), null, null));
//        }
//
//        if (entity.getCreatedBy() != null) {
//            UserInfoDTO userInfo = new UserInfoDTO();
//            userInfo.setId(entity.getCreatedBy().getId());
//            userInfo.setName(entity.getCreatedBy().getFullName());
//            userInfo.setEmail(entity.getCreatedBy().getEmail());
//            response.setCreatedBy(userInfo);
//        }
//
//        if (entity.getAccessLogs() != null) {
//            response.setAccessCount((long) entity.getAccessLogs().size());
//        }
//
//        if (entity.getShares() != null) {
//            response.setShares(entity.getShares().stream()
//                    .map(this::toDocumentShareResponse)
//                    .collect(Collectors.toList()));
//        }
//
//        return response;
//    }
//
//    // Upload Session Mappings
//    public UploadSession toUploadSessionEntity(UploadInitRequestDTO request, File file, String sessionId, String uploadId) {
//        return UploadSession.builder()
//                .sessionId(sessionId)
//                .uploadId(uploadId)
//                .file(file)
//                .chunkSize(request.getChunkSize())
//                .totalChunks(calculateTotalChunks(request.getFileSize(), request.getChunkSize()))
//                .totalSize(request.getFileSize())
//                .status(UploadStatus.INITIATED)
//                .expiresAt(java.time.LocalDateTime.now().plusHours(24))
//                .metadata(convertToJson(request.getMetadata()))
//                .build();
//    }
//
//    public UploadInitResponseDTO toUploadInitResponse(UploadSession entity, Map<String, String> uploadUrls) {
//        UploadInitResponseDTO response = new UploadInitResponseDTO();
//        response.setSessionId(entity.getSessionId());
//        response.setUploadId(entity.getUploadId());
//        response.setChunkSize(entity.getChunkSize());
//        response.setTotalChunks(entity.getTotalChunks());
//        response.setExpiresAt(entity.getExpiresAt());
//        response.setUploadUrls(uploadUrls);
//        return response;
//    }
//
//    public UploadStatusResponseDTO toUploadStatusResponse(UploadSession entity) {
//        UploadStatusResponseDTO response = new UploadStatusResponseDTO();
//        response.setSessionId(entity.getSessionId());
//        response.setUploadId(entity.getUploadId());
//        response.setStatus(entity.getStatus());
//        response.setUploadedChunks(entity.getUploadedChunks());
//        response.setTotalChunks(entity.getTotalChunks());
//        response.setUploadedSize(entity.getUploadedSize());
//        response.setTotalSize(entity.getTotalSize());
//        response.setProgressPercentage(entity.getProgressPercentage());
//        response.setExpiresAt(entity.getExpiresAt());
//        response.setErrorMessage(entity.getErrorMessage());
//        return response;
//    }
//
//    // Storage Mappings
//    public Storage toStorageEntity(StorageCreateRequestDTO request) {
//        return Storage.builder()
//                .name(request.getName())
//                .provider(request.getProvider())
//                .bucketName(request.getBucketName())
//                .region(request.getRegion())
//                .endpoint(request.getEndpoint())
//                .accessKey(request.getAccessKey())
//                .secretKey(request.getSecretKey())
//                .isDefault(request.getIsDefault() != null ? request.getIsDefault() : false)
//                .isActive(true)
//                .maxFileSize(request.getMaxFileSize())
//                .allowedExtensions(convertListToString(request.getAllowedExtensions() != null ?
//                        List.of(request.getAllowedExtensions()) : List.of()))
//                .quotaBytes(request.getQuotaBytes())
//                .config(convertToJson(request.getConfig()))
//                .build();
//    }
//
//    public StorageResponseDTO toStorageResponse(Storage entity) {
//        StorageResponseDTO response = new StorageResponseDTO();
//        response.setId(entity.getId());
//        response.setName(entity.getName());
//        response.setProvider(entity.getProvider());
//        response.setBucketName(entity.getBucketName());
//        response.setRegion(entity.getRegion());
//        response.setEndpoint(entity.getEndpoint());
//        response.setIsDefault(entity.getIsDefault());
//        response.setIsActive(entity.getIsActive());
//        response.setMaxFileSize(entity.getMaxFileSize());
//        response.setAllowedExtensions(entity.getAllowedExtensionsArray());
//        response.setQuotaBytes(entity.getQuotaBytes());
//        response.setUsedBytes(entity.getUsedBytes());
//        response.setUsagePercentage(entity.getUsagePercentage());
//        response.setCreatedAt(entity.getCreatedAt());
//        response.setUpdatedAt(entity.getUpdatedAt());
//        return response;
//    }
//
//    // Document Share Mappings
//    public DocumentShare toDocumentShareEntity(DocumentShareRequestDTO request, Document document, User sharedBy) {
//        return DocumentShare.builder()
//                .document(document)
//                .sharedWithUserId(request.getSharedWithUserId())
//                .sharedWithRole(request.getSharedWithRole())
//                .sharedWithEmail(request.getSharedWithEmail())
//                .permissionLevel(request.getPermissionLevel())
//                .accessCode(request.getAccessCode())
//                .expiresAt(request.getExpiresAt())
//                .isActive(true)
//                .sharedBy(sharedBy)
//                .build();
//    }
//
//    public DocumentShareResponseDTO toDocumentShareResponse(DocumentShare entity) {
//        DocumentShareResponseDTO response = new DocumentShareResponseDTO();
//        response.setId(entity.getId());
//        response.setDocumentId(entity.getDocument().getId());
//        response.setSharedWithUserId(entity.getSharedWithUserId());
//        response.setSharedWithRole(entity.getSharedWithRole());
//        response.setSharedWithEmail(entity.getSharedWithEmail());
//        response.setPermissionLevel(entity.getPermissionLevel());
//        response.setAccessCode(entity.getAccessCode());
//        response.setExpiresAt(entity.getExpiresAt());
//        response.setIsActive(entity.getIsActive());
//        response.setCreatedAt(entity.getCreatedAt());
//        response.setIsValid(entity.isValid());
//
//        if (entity.getSharedBy() != null) {
//            UserInfoDTO userInfo = new UserInfoDTO();
//            userInfo.setId(entity.getSharedBy().getId());
//            userInfo.setName(entity.getSharedBy().getFullName());
//            userInfo.setEmail(entity.getSharedBy().getEmail());
//            response.setSharedBy(userInfo);
//        }
//
//        return response;
//    }
//
//    // Helper methods
//    private String extractExtension(String fileName) {
//        if (fileName == null) return null;
//        int lastDotIndex = fileName.lastIndexOf('.');
//        return lastDotIndex > 0 ? fileName.substring(lastDotIndex + 1).toLowerCase() : null;
//    }
//
//    private java.time.LocalDateTime calculateExpiryDate(Integer retentionPeriod) {
//        if (retentionPeriod == null) return null;
//        return java.time.LocalDateTime.now().plusDays(retentionPeriod);
//    }
//
//    private Integer calculateTotalChunks(Long fileSize, Long chunkSize) {
//        if (chunkSize == null || fileSize == null) return 1;
//        return (int) Math.ceil((double) fileSize / chunkSize);
//    }
//
//    public String convertToJson(Object object) {
//        try {
//            return object != null ? objectMapper.writeValueAsString(object) : null;
//        } catch (Exception e) {
//            log.error("Error converting object to JSON", e);
//            return null;
//        }
//    }
//
//    private <T> T convertFromJson(String json, Class<T> type) {
//        try {
//            return json != null ? objectMapper.readValue(json, type) : null;
//        } catch (Exception e) {
//            log.error("Error converting JSON to object", e);
//            return null;
//        }
//    }
//
//    public String convertListToString(List<String> list) {
//        return list != null ? String.join(",", list) : null;
//    }
//
//    private List<String> convertStringToList(String str) {
//        return str != null ? List.of(str.split(",")) : null;
//    }
//}
