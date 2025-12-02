package com.itsutra.project.service;


import com.itsutra.project.dao.StorageDAO;
import com.itsutra.project.dto.StorageCreateRequestDTO;
import com.itsutra.project.dto.StorageQuotaUpdateRequestDTO;
import com.itsutra.project.dto.StorageResponseDTO;
import com.itsutra.project.dto.StorageStatsResponseDTO;
import com.itsutra.project.entity.Storage;
import com.itsutra.project.entity.User;
import com.itsutra.project.enums.FileCategory;
import com.itsutra.project.mapper.FileStorageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageService {

    private final StorageDAO storageDAO;
    private final FileStorageMapper fileStorageMapper;
    private final AuthenticationService authenticationService;

    @Value("${app.storage.local.base-path}")
    private String localStorageBasePath;

    @Value("${app.storage.default-provider}")
    private String defaultStorageProvider;


    private S3Client s3Client;
    private S3Presigner s3Presigner;

    @Transactional
    public StorageResponseDTO createStorageConfiguration(StorageCreateRequestDTO request) {
        log.info("Creating storage configuration: {}", request.getName());

        User user = authenticationService.getCurrentUser();
        if (storageDAO.findByName(request.getName()).isPresent()) {
            throw new IllegalArgumentException("Storage configuration with name already exists: " + request.getName());
        }

        Storage storage = fileStorageMapper.toStorageEntity(request);
        storage.setCreatedBy(user);
        Storage savedStorage = storageDAO.save(storage);

        log.info("Successfully created storage configuration with id: {}", savedStorage.getId());
        return fileStorageMapper.toStorageResponse(savedStorage);
    }


    @Transactional
    public List<StorageResponseDTO> getAllStorageConfigurations() {
        log.debug("Fetching all storage configurations");
        User user = authenticationService.getCurrentUser();
        return storageDAO.findByCreatedById(user.getId()).stream()
                .map(fileStorageMapper::toStorageResponse)
                .collect(Collectors.toList());
    }


    @Transactional
    public StorageResponseDTO getStorageConfiguration(Long id) {
        log.debug("Fetching storage configuration by id: {}", id);
        User user = authenticationService.getCurrentUser();
        Storage storage = storageDAO.findByIdAndCreatedById(id,user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Storage configuration not found with id: " + id));
        return fileStorageMapper.toStorageResponse(storage);
    }


    @Transactional
    public StorageResponseDTO updateStorageConfiguration(Long id, StorageCreateRequestDTO request) {
        log.info("Updating storage configuration with id: {}", id);

        User user = authenticationService.getCurrentUser();
        Storage storage = storageDAO.findByIdAndCreatedById(id,user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Storage configuration not found with id: " + id));

        // Update fields
        storage.setName(request.getName());
        storage.setProvider(request.getProvider());
        storage.setBucketName(request.getBucketName());
        storage.setRegion(request.getRegion());
        storage.setEndpoint(request.getEndpoint());
        storage.setAccessKey(request.getAccessKey());
        storage.setSecretKey(request.getSecretKey());
        storage.setMaxFileSize(request.getMaxFileSize());
        storage.setAllowedExtensions(String.join(",", request.getAllowedExtensions()));
        storage.setQuotaBytes(request.getQuotaBytes());
        storage.setConfig(fileStorageMapper.convertToJson(request.getConfig()));

        Storage updatedStorage = storageDAO.save(storage);
        log.info("Successfully updated storage configuration with id: {}", id);
        return fileStorageMapper.toStorageResponse(updatedStorage);
    }


    @Transactional
    public StorageResponseDTO updateStorageQuota(Long id, StorageQuotaUpdateRequestDTO request) {
        log.info("Updating storage quota for configuration id: {}", id);

        User user = authenticationService.getCurrentUser();
        Storage storage = storageDAO.findByIdAndCreatedById(id,user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Storage configuration not found with id: " + id));

        storage.setQuotaBytes(request.getQuotaBytes());
        Storage updatedStorage = storageDAO.save(storage);

        log.info("Successfully updated storage quota for configuration id: {}", id);
        return fileStorageMapper.toStorageResponse(updatedStorage);
    }


    @Transactional
    public StorageStatsResponseDTO getStorageStats() {
        log.debug("Fetching storage statistics");

        StorageStatsResponseDTO stats = new StorageStatsResponseDTO();

        // Calculate total statistics
        Long totalStorageUsed = storageDAO.findAll().stream()
                .mapToLong(Storage::getUsedBytes)
                .sum();
        Long totalQuota = storageDAO.findAll().stream()
                .mapToLong(storage -> storage.getQuotaBytes() != null ? storage.getQuotaBytes() : 0)
                .sum();

        stats.setTotalStorageUsed(totalStorageUsed);
        stats.setTotalQuota(totalQuota);
        stats.setOverallUsagePercentage(totalQuota > 0 ? (double) totalStorageUsed / totalQuota * 100 : 0);

        // Get provider distribution
        List<Object[]> providerDistribution = storageDAO.getStorageUsageByProvider();
        stats.setProviderDistribution(providerDistribution.stream()
                .collect(Collectors.toMap(
                        arr -> arr[0].toString(),
                        arr -> (Long) arr[2] // storage used
                )));

        return stats;
    }


    @Transactional
    public Map<String, Object> getStorageHealth() {

        List<Storage> activeStorages = storageDAO.findByIsActive(true);
        Map<String, Object> health = new HashMap<>();

        health.put("activeConfigurations", activeStorages.size());
        health.put("lastChecked", java.time.LocalDateTime.now());

        // Check connectivity for each storage
        List<Map<String, Object>> storageHealth = new ArrayList<>();
        for (Storage storage : activeStorages) {
            Map<String, Object> storageStatus = new HashMap<>();
            storageStatus.put("id", storage.getId());
            storageStatus.put("name", storage.getName());
            storageStatus.put("provider", storage.getProvider());
            storageStatus.put("status", checkStorageConnectivity(storage));
            storageHealth.add(storageStatus);
        }

        health.put("storages", storageHealth);
        health.put("overallStatus", determineOverallHealth(storageHealth));

        return health;
    }


    @Transactional
    public void deleteStorageConfiguration(Long id) {
        log.info("Deleting storage configuration with id: {}", id);
        User user = authenticationService.getCurrentUser();
        Storage storage = storageDAO.findByIdAndCreatedById(id,user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Storage configuration not found with id: " + id));

        storage.setIsActive(false);
        storageDAO.save(storage);
        log.info("Successfully deactivated storage configuration with id: {}", id);
    }













    // Storage Provider Implementation
    public String storeFile(MultipartFile file, String storageKey, FileCategory category) {
        log.info("Storing file: {} with key: {} in category: {}",
                file.getOriginalFilename(), storageKey, category);

        try {
            Storage storage = getActiveStorage();
            String filePath = buildFilePath(category, storageKey);

            switch (storage.getProvider().toUpperCase()) {
                case "AWS_S3":
                    return storeFileInS3(file, storageKey, storage, filePath);
                case "AZURE_BLOB":
                    return storeFileInAzure(file, storageKey, storage, filePath);
                case "GOOGLE_CLOUD_STORAGE":
                    return storeFileInGCS(file, storageKey, storage, filePath);
                case "LOCAL":
                default:
                    return storeFileLocally(file, storageKey, filePath);
            }
        } catch (Exception e) {
            log.error("Error storing file: {}", storageKey, e);
            throw new RuntimeException("File storage failed: " + e.getMessage(), e);
        }
    }

    public String generateDownloadUrl(String storageKey) {
        log.debug("Generating download URL for storage key: {}", storageKey);

        try {
            Storage storage = getActiveStorage();
            String filePath = findFilePath(storageKey);

            switch (storage.getProvider().toUpperCase()) {
                case "AWS_S3":
                    return generateS3DownloadUrl(storage, filePath);
                case "AZURE_BLOB":
                    return generateAzureDownloadUrl(storage, filePath);
                case "GOOGLE_CLOUD_STORAGE":
                    return generateGCSDownloadUrl(storage, filePath);
                case "LOCAL":
                default:
                    return generateLocalDownloadUrl(filePath);
            }
        } catch (Exception e) {
            log.error("Error generating download URL for: {}", storageKey, e);
            throw new RuntimeException("Download URL generation failed: " + e.getMessage(), e);
        }
    }

    public String generateUploadUrl(String sessionId) {
        log.debug("Generating upload URL for session: {}", sessionId);

        try {
            Storage storage = getActiveStorage();
            String filePath = "uploads/" + sessionId;

            switch (storage.getProvider().toUpperCase()) {
                case "AWS_S3":
                    return generateS3UploadUrl(storage, filePath);
                case "AZURE_BLOB":
                    return generateAzureUploadUrl(storage, filePath);
                case "GOOGLE_CLOUD_STORAGE":
                    return generateGCSUploadUrl(storage, filePath);
                case "LOCAL":
                default:
                    return generateLocalUploadUrl(filePath);
            }
        } catch (Exception e) {
            log.error("Error generating upload URL for session: {}", sessionId, e);
            throw new RuntimeException("Upload URL generation failed: " + e.getMessage(), e);
        }
    }

    public String generateChunkUploadUrl(String sessionId, int chunkNumber, Long chunkSize) {
        log.debug("Generating chunk upload URL for session: {}, chunk: {}", sessionId, chunkNumber);

        try {
            Storage storage = getActiveStorage();
            String chunkPath = "chunks/" + sessionId + "/" + chunkNumber;

            switch (storage.getProvider().toUpperCase()) {
                case "AWS_S3":
                    return generateS3UploadUrl(storage, chunkPath);
                case "AZURE_BLOB":
                    return generateAzureUploadUrl(storage, chunkPath);
                case "GOOGLE_CLOUD_STORAGE":
                    return generateGCSUploadUrl(storage, chunkPath);
                case "LOCAL":
                default:
                    return generateLocalUploadUrl(chunkPath);
            }
        } catch (Exception e) {
            log.error("Error generating chunk upload URL for session: {}", sessionId, e);
            throw new RuntimeException("Chunk upload URL generation failed: " + e.getMessage(), e);
        }
    }

    public void storeChunk(String sessionId, int chunkNumber, MultipartFile chunk, String checksum) {
        log.debug("Storing chunk {} for session: {}", chunkNumber, sessionId);

        try {
            Storage storage = getActiveStorage();
            String chunkPath = "chunks/" + sessionId + "/" + chunkNumber;

            switch (storage.getProvider().toUpperCase()) {
                case "AWS_S3":
                    storeChunkInS3(chunk, chunkPath, storage);
                    break;
                case "AZURE_BLOB":
                    storeChunkInAzure(chunk, chunkPath, storage);
                    break;
                case "GOOGLE_CLOUD_STORAGE":
                    storeChunkInGCS(chunk, chunkPath, storage);
                    break;
                case "LOCAL":
                default:
                    storeChunkLocally(chunk, chunkPath);
                    break;
            }

            log.debug("Successfully stored chunk {} for session: {}", chunkNumber, sessionId);
        } catch (Exception e) {
            log.error("Error storing chunk for session: {}", sessionId, e);
            throw new RuntimeException("Chunk storage failed: " + e.getMessage(), e);
        }
    }

    public String combineChunks(String sessionId, int totalChunks, String finalChecksum) {
        log.info("Combining {} chunks for session: {}", totalChunks, sessionId);

        try {
            Storage storage = getActiveStorage();
            String finalFilePath = "uploads/" + sessionId + "/final";
            List<String> chunkPaths = new ArrayList<>();

            // Collect all chunk paths
            for (int i = 0; i < totalChunks; i++) {
                chunkPaths.add("chunks/" + sessionId + "/" + i);
            }

            switch (storage.getProvider().toUpperCase()) {
                case "AWS_S3":
                    return combineChunksInS3(sessionId, chunkPaths, finalFilePath, storage);
                case "AZURE_BLOB":
                    return combineChunksInAzure(sessionId, chunkPaths, finalFilePath, storage);
                case "GOOGLE_CLOUD_STORAGE":
                    return combineChunksInGCS(sessionId, chunkPaths, finalFilePath, storage);
                case "LOCAL":
                default:
                    return combineChunksLocally(sessionId, chunkPaths, finalFilePath);
            }
        } catch (Exception e) {
            log.error("Error combining chunks for session: {}", sessionId, e);
            throw new RuntimeException("Chunk combination failed: " + e.getMessage(), e);
        }
    }

    public void cleanupChunks(String sessionId) {
        log.info("Cleaning up chunks for session: {}", sessionId);

        try {
            Storage storage = getActiveStorage();
            String chunkPrefix = "chunks/" + sessionId + "/";

            switch (storage.getProvider().toUpperCase()) {
                case "AWS_S3":
                    cleanupChunksInS3(chunkPrefix, storage);
                    break;
                case "AZURE_BLOB":
                    cleanupChunksInAzure(chunkPrefix, storage);
                    break;
                case "GOOGLE_CLOUD_STORAGE":
                    cleanupChunksInGCS(chunkPrefix, storage);
                    break;
                case "LOCAL":
                default:
                    cleanupChunksLocally(chunkPrefix);
                    break;
            }

            log.info("Successfully cleaned up chunks for session: {}", sessionId);
        } catch (Exception e) {
            log.error("Error cleaning up chunks for session: {}", sessionId, e);
            // Don't throw exception for cleanup failures
        }
    }

    public void deleteFile(String storageKey) {
        log.info("Deleting file with storage key: {}", storageKey);

        try {
            Storage storage = getActiveStorage();
            String filePath = findFilePath(storageKey);

            switch (storage.getProvider().toUpperCase()) {
                case "AWS_S3":
                    deleteFileFromS3(filePath, storage);
                    break;
                case "AZURE_BLOB":
                    deleteFileFromAzure(filePath, storage);
                    break;
                case "GOOGLE_CLOUD_STORAGE":
                    deleteFileFromGCS(filePath, storage);
                    break;
                case "LOCAL":
                default:
                    deleteFileLocally(filePath);
                    break;
            }

            log.info("Successfully deleted file with storage key: {}", storageKey);
        } catch (Exception e) {
            log.error("Error deleting file with storage key: {}", storageKey, e);
            throw new RuntimeException("File deletion failed: " + e.getMessage(), e);
        }
    }

    // AWS S3 Implementation
    private String storeFileInS3(MultipartFile file, String storageKey, Storage storage, String filePath) throws IOException {
        initializeS3Client(storage);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(storage.getBucketName())
                .key(filePath)
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .metadata(Map.of(
                        "original-filename", file.getOriginalFilename(),
                        "storage-key", storageKey
                ))
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        return filePath;
    }

    private String generateS3DownloadUrl(Storage storage, String filePath) {
        initializeS3Client(storage);

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(storage.getBucketName())
                .key(filePath)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofHours(24))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
        return presignedRequest.url().toString();
    }

    private String generateS3UploadUrl(Storage storage, String filePath) {
        initializeS3Client(storage);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(storage.getBucketName())
                .key(filePath)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofHours(24))
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
        return presignedRequest.url().toString();
    }

    private void storeChunkInS3(MultipartFile chunk, String chunkPath, Storage storage) throws IOException {
        initializeS3Client(storage);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(storage.getBucketName())
                .key(chunkPath)
                .contentType(chunk.getContentType())
                .contentLength(chunk.getSize())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(chunk.getBytes()));
    }

    private String combineChunksInS3(String sessionId, List<String> chunkPaths, String finalFilePath, Storage storage) {
        initializeS3Client(storage);

        // For S3, we can use multipart upload or simply copy chunks to final location
        // This is a simplified implementation
        try {
            // In a real implementation, you would use S3's multipart upload capabilities
            // For now, we'll just copy the first chunk as a placeholder
            if (!chunkPaths.isEmpty()) {
                CopyObjectRequest copyRequest = CopyObjectRequest.builder()
                        .sourceBucket(storage.getBucketName())
                        .sourceKey(chunkPaths.get(0))
                        .destinationBucket(storage.getBucketName())
                        .destinationKey(finalFilePath)
                        .build();

                s3Client.copyObject(copyRequest);
            }

            return finalFilePath;
        } catch (Exception e) {
            throw new RuntimeException("Failed to combine chunks in S3", e);
        }
    }

    private void cleanupChunksInS3(String chunkPrefix, Storage storage) {
        initializeS3Client(storage);

        try {
            ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                    .bucket(storage.getBucketName())
                    .prefix(chunkPrefix)
                    .build();

            ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);

            for (S3Object s3Object : listResponse.contents()) {
                DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                        .bucket(storage.getBucketName())
                        .key(s3Object.key())
                        .build();

                s3Client.deleteObject(deleteRequest);
            }
        } catch (Exception e) {
            log.error("Error cleaning up S3 chunks with prefix: {}", chunkPrefix, e);
        }
    }

    private void deleteFileFromS3(String filePath, Storage storage) {
        initializeS3Client(storage);

        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(storage.getBucketName())
                .key(filePath)
                .build();

        s3Client.deleteObject(deleteRequest);
    }

    // Local File System Implementation
    private String storeFileLocally(MultipartFile file, String storageKey, String filePath) throws IOException {
        Path storagePath = Paths.get(localStorageBasePath, filePath);
        Files.createDirectories(storagePath.getParent());
        Files.write(storagePath, file.getBytes());
        return filePath;
    }

    private String generateLocalDownloadUrl(String filePath) {
        return "/api/files/download/local/" + Base64.getEncoder().encodeToString(filePath.getBytes());
    }

    private String generateLocalUploadUrl(String filePath) {
        return "/api/uploads/local/" + Base64.getEncoder().encodeToString(filePath.getBytes());
    }

    private void storeChunkLocally(MultipartFile chunk, String chunkPath) throws IOException {
        Path storagePath = Paths.get(localStorageBasePath, chunkPath);
        Files.createDirectories(storagePath.getParent());
        Files.write(storagePath, chunk.getBytes());
    }

    private String combineChunksLocally(String sessionId, List<String> chunkPaths, String finalFilePath) throws IOException {
        Path finalPath = Paths.get(localStorageBasePath, finalFilePath);
        Files.createDirectories(finalPath.getParent());

        try (var outputStream = Files.newOutputStream(finalPath)) {
            for (String chunkPath : chunkPaths) {
                Path chunkFilePath = Paths.get(localStorageBasePath, chunkPath);
                if (Files.exists(chunkFilePath)) {
                    Files.copy(chunkFilePath, outputStream);
                }
            }
        }

        return finalFilePath;
    }

    private void cleanupChunksLocally(String chunkPrefix) throws IOException {
        Path chunkDir = Paths.get(localStorageBasePath, chunkPrefix).getParent();
        if (Files.exists(chunkDir)) {
            Files.walk(chunkDir)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(file -> {
                        if (!file.delete()) {
                            log.warn("Failed to delete file: {}", file.getAbsolutePath());
                        }
                    });
        }
    }

    private void deleteFileLocally(String filePath) throws IOException {
        Path fileToDelete = Paths.get(localStorageBasePath, filePath);
        if (Files.exists(fileToDelete)) {
            Files.delete(fileToDelete);
        }
    }

    // Azure Blob Storage Implementation (Placeholder)
    private String storeFileInAzure(MultipartFile file, String storageKey, Storage storage, String filePath) {
        // Implementation for Azure Blob Storage
        log.info("Storing file in Azure Blob Storage: {}", filePath);
        return filePath;
    }

    private String generateAzureDownloadUrl(Storage storage, String filePath) {
        // Implementation for Azure Blob Storage
        return "/api/v1/files/download/azure/" + Base64.getEncoder().encodeToString(filePath.getBytes());
    }

    private String generateAzureUploadUrl(Storage storage, String filePath) {
        // Implementation for Azure Blob Storage
        return "/api/v1/uploads/azure/" + Base64.getEncoder().encodeToString(filePath.getBytes());
    }

    private void storeChunkInAzure(MultipartFile chunk, String chunkPath, Storage storage) {
        // Implementation for Azure Blob Storage
        log.debug("Storing chunk in Azure: {}", chunkPath);
    }

    private String combineChunksInAzure(String sessionId, List<String> chunkPaths, String finalFilePath, Storage storage) {
        // Implementation for Azure Blob Storage
        log.info("Combining chunks in Azure for session: {}", sessionId);
        return finalFilePath;
    }

    private void cleanupChunksInAzure(String chunkPrefix, Storage storage) {
        // Implementation for Azure Blob Storage
        log.info("Cleaning up Azure chunks with prefix: {}", chunkPrefix);
    }

    private void deleteFileFromAzure(String filePath, Storage storage) {
        // Implementation for Azure Blob Storage
        log.info("Deleting file from Azure: {}", filePath);
    }

    // Google Cloud Storage Implementation (Placeholder)
    private String storeFileInGCS(MultipartFile file, String storageKey, Storage storage, String filePath) {
        // Implementation for Google Cloud Storage
        log.info("Storing file in Google Cloud Storage: {}", filePath);
        return filePath;
    }

    private String generateGCSDownloadUrl(Storage storage, String filePath) {
        // Implementation for Google Cloud Storage
        return "/api/v1/files/download/gcs/" + Base64.getEncoder().encodeToString(filePath.getBytes());
    }

    private String generateGCSUploadUrl(Storage storage, String filePath) {
        // Implementation for Google Cloud Storage
        return "/api/v1/uploads/gcs/" + Base64.getEncoder().encodeToString(filePath.getBytes());
    }

    private void storeChunkInGCS(MultipartFile chunk, String chunkPath, Storage storage) {
        // Implementation for Google Cloud Storage
        log.debug("Storing chunk in GCS: {}", chunkPath);
    }

    private String combineChunksInGCS(String sessionId, List<String> chunkPaths, String finalFilePath, Storage storage) {
        // Implementation for Google Cloud Storage
        log.info("Combining chunks in GCS for session: {}", sessionId);
        return finalFilePath;
    }

    private void cleanupChunksInGCS(String chunkPrefix, Storage storage) {
        // Implementation for Google Cloud Storage
        log.info("Cleaning up GCS chunks with prefix: {}", chunkPrefix);
    }

    private void deleteFileFromGCS(String filePath, Storage storage) {
        // Implementation for Google Cloud Storage
        log.info("Deleting file from GCS: {}", filePath);
    }

    // Helper Methods
    private Storage getActiveStorage() {
        return storageDAO.findByIsActive(true).stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No active storage configuration found"));
    }

    private String buildFilePath(FileCategory category, String storageKey) {
        return category.name().toLowerCase() + "/" + storageKey;
    }

    private String findFilePath(String storageKey) {
        // In a real implementation, you would query the database for the file path
        // For now, return a constructed path
        return "files/" + storageKey;
    }

    private void initializeS3Client(Storage storage) {
        if (s3Client == null && storage.getAccessKey() != null && storage.getSecretKey() != null) {
            AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
                    storage.getAccessKey(),
                    storage.getSecretKey()
            );

            s3Client = S3Client.builder()
                    .region(Region.of(storage.getRegion()))
                    .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                    .build();

            s3Presigner = S3Presigner.builder()
                    .region(Region.of(storage.getRegion()))
                    .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                    .build();
        }
    }







    private String checkStorageConnectivity(Storage storage) {
        try {
            switch (storage.getProvider().toUpperCase()) {
                case "AWS_S3":
                    return checkS3Connectivity(storage);
                case "AZURE_BLOB":
                    return checkAzureConnectivity(storage);
                case "GOOGLE_CLOUD_STORAGE":
                    return checkGCSConnectivity(storage);
                case "LOCAL":
                    return checkLocalConnectivity();
                default:
                    return "UNKNOWN";
            }
        } catch (Exception e) {
            log.error("Error checking connectivity for storage: {}", storage.getName(), e);
            return "UNHEALTHY";
        }
    }

    private String checkS3Connectivity(Storage storage) {
        try {
            initializeS3Client(storage);
            s3Client.listBuckets();
            return "HEALTHY";
        } catch (Exception e) {
            return "UNHEALTHY";
        }
    }

    private String checkAzureConnectivity(Storage storage) {
        // Implementation for Azure connectivity check
        return "HEALTHY"; // Placeholder
    }

    private String checkGCSConnectivity(Storage storage) {
        // Implementation for GCS connectivity check
        return "HEALTHY"; // Placeholder
    }

    private String checkLocalConnectivity() {
        try {
            Path storagePath = Paths.get(localStorageBasePath);
            if (!Files.exists(storagePath)) {
                Files.createDirectories(storagePath);
            }
            return "HEALTHY";
        } catch (Exception e) {
            return "UNHEALTHY";
        }
    }

    private String determineOverallHealth(List<Map<String, Object>> storageHealth) {
        boolean allHealthy = storageHealth.stream()
                .allMatch(status -> "HEALTHY".equals(status.get("status")));
        return allHealthy ? "HEALTHY" : "DEGRADED";
    }
}
