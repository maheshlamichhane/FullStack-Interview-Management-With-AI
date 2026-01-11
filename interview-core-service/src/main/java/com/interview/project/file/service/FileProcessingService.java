package com.interview.project.file.service;//package com.itsutra.project.file.service;
//
//
//import com.itsutra.project.file.dao.FileDAO;
//import com.itsutra.project.file.dto.FileConversionRequestDTO;
//import com.itsutra.project.file.dto.FileConversionResponseDTO;
//import com.itsutra.project.file.entity.File;
//import com.itsutra.project.file.enums.FileStatus;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.tika.Tika;
//import org.apache.tika.metadata.Metadata;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
//import javax.imageio.ImageIO;
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.CompletableFuture;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class FileProcessingService {
//
//    private final FileDAO fileDAO;
//    private final StorageService storageService;
//    private final Tika tika = new Tika();
//
//    /**
//     * Process file asynchronously - extract metadata, generate previews, etc.
//     */
//    @Async
//    public CompletableFuture<Void> processFileAsync(File file) {
//        log.info("Starting async processing for file: {}", file.getName());
//
//        try {
//            // Extract metadata
//            Map<String, Object> metadata = extractFileMetadata(file);
//            log.debug("Extracted metadata for file {}: {}", file.getId(), metadata);
//
//            // Generate preview for images and documents
//            if (file.isImage() || file.isDocument()) {
//                generatePreview(file);
//            }
//
//            // Extract text content for search indexing
//            if (file.isDocument()) {
//                String textContent = extractTextContent(file);
//                log.debug("Extracted text content for file {}: {} characters", file.getId(), textContent.length());
//            }
//
//            // Validate file integrity
//            boolean isValid = validateFileIntegrity(file);
//            if (!isValid) {
//                log.warn("File integrity check failed for file: {}", file.getId());
//                file.setStatus(FileStatus.QUARANTINED);
//            } else {
//                file.setStatus(FileStatus.ACTIVE);
//            }
//
//            fileDAO.save(file);
//            log.info("Successfully processed file: {}", file.getName());
//
//        } catch (Exception e) {
//            log.error("Error processing file: {}", file.getId(), e);
//            file.setStatus(FileStatus.QUARANTINED);
//            fileDAO.save(file);
//        }
//
//        return CompletableFuture.completedFuture(null);
//    }
//
//    /**
//     * Generate preview URL for a file
//     */
//    public String generatePreviewUrl(File file) {
//        if (!file.isImage() && !file.isDocument()) {
//            return null;
//        }
//
//        try {
//            // Check if preview already exists
//            String previewPath = "previews/" + file.getStorageKey() + "_preview.jpg";
//            Path localPreviewPath = Paths.get("/tmp/previews", previewPath);
//
//            if (!Files.exists(localPreviewPath)) {
//                // Generate preview if it doesn't exist
//                generatePreview(file);
//            }
//
//            return storageService.generateDownloadUrl(previewPath);
//        } catch (Exception e) {
//            log.error("Error generating preview URL for file: {}", file.getId(), e);
//            return null;
//        }
//    }
//
//    /**
//     * Extract metadata from file
//     */
//    public Map<String, Object> extractFileMetadata(File file) {
//        Map<String, Object> metadata = new HashMap<>();
//
//        try {
//            // Use Apache Tika for metadata extraction
//            Metadata tikaMetadata = new Metadata();
//            String filePath = getFilePath(file);
//
//            try (InputStream inputStream = getFileInputStream(file)) {
//                tika.parse(inputStream, tikaMetadata);
//
//                // Extract common metadata
//                metadata.put("contentType", tikaMetadata.get(Metadata.CONTENT_TYPE));
//                metadata.put("title", tikaMetadata.get("title"));
//                metadata.put("author", tikaMetadata.get("Author"));
//                metadata.put("creationDate", tikaMetadata.get("creationDate"));
//                metadata.put("lastModified", tikaMetadata.get("lastModified"));
//                metadata.put("pageCount", tikaMetadata.get("pageCount"));
//                metadata.put("wordCount", tikaMetadata.get("wordCount"));
//
//                // Extract image-specific metadata
//                if (file.isImage()) {
//                    metadata.put("width", tikaMetadata.get("width"));
//                    metadata.put("height", tikaMetadata.get("height"));
//                    metadata.put("colorSpace", tikaMetadata.get("colorSpace"));
//                }
//
//                // Extract PDF-specific metadata
//                if ("application/pdf".equals(file.getMimeType())) {
//                    metadata.put("pdfVersion", tikaMetadata.get("pdfVersion"));
//                    metadata.put("encrypted", tikaMetadata.get("encrypted"));
//                }
//            }
//
//            // Add custom metadata
//            metadata.put("fileSize", file.getSize());
//            metadata.put("extension", file.getExtension());
//            metadata.put("processingTime", LocalDateTime.now().toString());
//
//        } catch (Exception e) {
//            log.error("Error extracting metadata for file: {}", file.getId(), e);
//            metadata.put("error", "Metadata extraction failed: " + e.getMessage());
//        }
//
//        return metadata;
//    }
//
//    /**
//     * Generate preview for images and documents
//     */
//    public void generatePreview(File file) {
//        try {
//            if (file.isImage()) {
//                generateImagePreview(file);
//            } else if (file.isDocument() && "application/pdf".equals(file.getMimeType())) {
//                generatePdfPreview(file);
//            } else if (file.isDocument() && file.getMimeType().contains("word")) {
//                generateDocumentPreview(file);
//            }
//        } catch (Exception e) {
//            log.error("Error generating preview for file: {}", file.getId(), e);
//        }
//    }
//
//    /**
//     * Generate image preview (thumbnail)
//     */
//    private void generateImagePreview(File file) throws IOException {
//        try (InputStream inputStream = getFileInputStream(file)) {
//            BufferedImage originalImage = ImageIO.read(inputStream);
//
//            if (originalImage != null) {
//                // Create thumbnail
//                int thumbWidth = 200;
//                int thumbHeight = (int) ((double) originalImage.getHeight() / originalImage.getWidth() * thumbWidth);
//
//                BufferedImage thumbImage = new BufferedImage(thumbWidth, thumbHeight, BufferedImage.TYPE_INT_RGB);
//                Graphics2D graphics2D = thumbImage.createGraphics();
//                graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//                graphics2D.drawImage(originalImage, 0, 0, thumbWidth, thumbHeight, null);
//                graphics2D.dispose();
//
//                // Save thumbnail
//                String previewPath = "previews/" + file.getStorageKey() + "_preview.jpg";
//                Path localPreviewPath = Paths.get("/tmp/previews", previewPath);
//                Files.createDirectories(localPreviewPath.getParent());
//
//                try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
//                    ImageIO.write(thumbImage, "jpg", outputStream);
//                    Files.write(localPreviewPath, outputStream.toByteArray());
//                }
//
//                log.debug("Generated image preview for file: {}", file.getId());
//            }
//        }
//    }
//
//    /**
//     * Generate PDF preview (first page as image)
//     */
//    private void generatePdfPreview(File file) {
//        // Implementation would use a PDF library like Apache PDFBox
//        // For now, create a placeholder
//        log.debug("PDF preview generation for file: {} - Not implemented", file.getId());
//    }
//
//    /**
//     * Generate document preview
//     */
//    private void generateDocumentPreview(File file) {
//        // Implementation would use a document processing library
//        // For now, create a placeholder
//        log.debug("Document preview generation for file: {} - Not implemented", file.getId());
//    }
//
//    /**
//     * Extract text content for search indexing
//     */
//    public String extractTextContent(File file) {
//        try (InputStream inputStream = getFileInputStream(file)) {
//            return tika.parseToString(inputStream);
//        } catch (Exception e) {
//            log.error("Error extracting text content for file: {}", file.getId(), e);
//            return "";
//        }
//    }
//
//    /**
//     * Validate file integrity
//     */
//    public boolean validateFileIntegrity(File file) {
//        try {
//            // Check file size matches
//            long actualSize = getFileSize(file);
//            if (actualSize != file.getSize()) {
//                log.warn("File size mismatch for file {}: expected {}, actual {}",
//                        file.getId(), file.getSize(), actualSize);
//                return false;
//            }
//
//            // Check if file is readable
//            if (!isFileReadable(file)) {
//                log.warn("File is not readable: {}", file.getId());
//                return false;
//            }
//
//            // Check for malicious content patterns
//            if (detectMaliciousContent(file)) {
//                log.warn("Potential malicious content detected in file: {}", file.getId());
//                return false;
//            }
//
//            return true;
//        } catch (Exception e) {
//            log.error("Error validating file integrity: {}", file.getId(), e);
//            return false;
//        }
//    }
//
//    /**
//     * Convert file to different format
//     */
//    public FileConversionResponseDTO convertFile(Long fileId, FileConversionRequestDTO request) {
//        log.info("Converting file {} to format: {}", fileId, request.getTargetFormat());
//
//        try {
//            File originalFile = fileDAO.findById(fileId)
//                    .orElseThrow(() -> new IllegalArgumentException("File not found with id: " + fileId));
//
//            // Perform conversion based on target format
//            byte[] convertedData = performConversion(originalFile, request);
//
//            // Store converted file
//            String convertedStorageKey = generateConvertedStorageKey(originalFile, request.getTargetFormat());
//            String convertedPath = storageService.storeFile(
//                    createMultipartFile(convertedData, getConvertedFileName(originalFile, request.getTargetFormat())),
//                    convertedStorageKey,
//                    originalFile.getCategory()
//            );
//
//            // Create response
//            FileConversionResponseDTO response = new FileConversionResponseDTO();
//            response.setOriginalFileId(String.valueOf(originalFile.getId()));
//            response.setConvertedFileId(convertedStorageKey); // In production, you'd create a new File entity
//            response.setTargetFormat(request.getTargetFormat());
//            response.setDownloadUrl(storageService.generateDownloadUrl(convertedStorageKey));
//            response.setFileSize((long) convertedData.length);
//            response.setConvertedAt(LocalDateTime.now());
//            response.setStatus("COMPLETED");
//
//            log.info("Successfully converted file {} to {}", fileId, request.getTargetFormat());
//            return response;
//
//        } catch (Exception e) {
//            log.error("Error converting file {}: {}", fileId, e.getMessage(), e);
//            FileConversionResponseDTO response = new FileConversionResponseDTO();
//            response.setStatus("FAILED");
//            return response;
//        }
//    }
//
//    /**
//     * Compress file
//     */
//    public File compressFile(Long fileId, Integer quality) {
//        log.info("Compressing file: {} with quality: {}", fileId, quality);
//
//        try {
//            File originalFile = fileDAO.findById(fileId)
//                    .orElseThrow(() -> new IllegalArgumentException("File not found with id: " + fileId));
//
//            if (!originalFile.isImage()) {
//                throw new IllegalArgumentException("Compression is only supported for images");
//            }
//
//            // Perform compression
//            byte[] compressedData = performCompression(originalFile, quality);
//
//            // Create compressed file entity
//            File compressedFile = File.builder()
//                    .name(getCompressedFileName(originalFile))
//                    .originalName(getCompressedFileName(originalFile))
//                    .storageKey(generateCompressedStorageKey(originalFile))
//                    .mimeType(originalFile.getMimeType())
//                    .size((long) compressedData.length)
//                    .extension(originalFile.getExtension())
//                    .category(originalFile.getCategory())
//                    .status(FileStatus.ACTIVE)
//                    .storageProvider(originalFile.getStorageProvider())
//                    .uploadedBy(originalFile.getUploadedBy())
//                    .build();
//
//            // Store compressed file
//            String storagePath = storageService.storeFile(
//                    createMultipartFile(compressedData, compressedFile.getOriginalName()),
//                    compressedFile.getStorageKey(),
//                    compressedFile.getCategory()
//            );
//            compressedFile.setStoragePath(storagePath);
//
//            File savedFile = fileDAO.save(compressedFile);
//            log.info("Successfully compressed file: {}", fileId);
//            return savedFile;
//
//        } catch (Exception e) {
//            log.error("Error compressing file {}: {}", fileId, e.getMessage(), e);
//            throw new RuntimeException("File compression failed: " + e.getMessage(), e);
//        }
//    }
//
//    // Helper Methods
//    private String getFilePath(File file) {
//        return file.getStoragePath() != null ? file.getStoragePath() : file.getStorageKey();
//    }
//
//    private InputStream getFileInputStream(File file) throws IOException {
//        // Implementation would retrieve file from storage
//        // For now, return a placeholder
//        return new ByteArrayInputStream(new byte[0]);
//    }
//
//    private long getFileSize(File file) throws IOException {
//        // Implementation would get actual file size from storage
//        return file.getSize(); // Placeholder
//    }
//
//    private boolean isFileReadable(File file) {
//        try (InputStream ignored = getFileInputStream(file)) {
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    private boolean detectMaliciousContent(File file) {
//        // Basic malware detection - in production, use proper antivirus scanning
//        try {
//            String content = extractTextContent(file);
//
//            // Check for common malicious patterns (simplified)
//            String[] suspiciousPatterns = {
//                    "javascript:", "vbscript:", "<script", "eval(", "base64_decode",
//                    "shell_exec", "system(", "exec(", "passthru("
//            };
//
//            for (String pattern : suspiciousPatterns) {
//                if (content.toLowerCase().contains(pattern)) {
//                    return true;
//                }
//            }
//
//            return false;
//        } catch (Exception e) {
//            log.warn("Error during malicious content detection for file: {}", file.getId(), e);
//            return false; // Be conservative - don't flag as malicious if we can't check
//        }
//    }
//
//    private byte[] performConversion(File originalFile, FileConversionRequestDTO request) {
//        // Implementation would use appropriate libraries for conversion
//        // For now, return placeholder
//        log.debug("Converting file {} to {}", originalFile.getId(), request.getTargetFormat());
//        return new byte[0];
//    }
//
//    private byte[] performCompression(File originalFile, Integer quality) {
//        // Implementation would compress the image
//        // For now, return placeholder
//        log.debug("Compressing file {} with quality {}", originalFile.getId(), quality);
//        return new byte[0];
//    }
//
//    private String generateConvertedStorageKey(File originalFile, String targetFormat) {
//        return "converted_" + originalFile.getStorageKey() + "_" +
//                System.currentTimeMillis() + "." + targetFormat.toLowerCase();
//    }
//
//    private String generateCompressedStorageKey(File originalFile) {
//        return "compressed_" + originalFile.getStorageKey() + "_" + System.currentTimeMillis();
//    }
//
//    private String getConvertedFileName(File originalFile, String targetFormat) {
//        String baseName = originalFile.getOriginalName();
//        int lastDotIndex = baseName.lastIndexOf('.');
//        if (lastDotIndex > 0) {
//            baseName = baseName.substring(0, lastDotIndex);
//        }
//        return baseName + "." + targetFormat.toLowerCase();
//    }
//
//    private String getCompressedFileName(File originalFile) {
//        String baseName = originalFile.getOriginalName();
//        int lastDotIndex = baseName.lastIndexOf('.');
//        if (lastDotIndex > 0) {
//            String extension = baseName.substring(lastDotIndex);
//            String nameWithoutExtension = baseName.substring(0, lastDotIndex);
//            return nameWithoutExtension + "_compressed" + extension;
//        }
//        return baseName + "_compressed";
//    }
//
//    private org.springframework.web.multipart.MultipartFile createMultipartFile(byte[] data, String fileName) {
//        return new org.springframework.web.multipart.MultipartFile() {
//            @Override
//            public String getName() {
//                return "file";
//            }
//
//            @Override
//            public String getOriginalFilename() {
//                return fileName;
//            }
//
//            @Override
//            public String getContentType() {
//                return "application/octet-stream";
//            }
//
//            @Override
//            public boolean isEmpty() {
//                return data.length == 0;
//            }
//
//            @Override
//            public long getSize() {
//                return data.length;
//            }
//
//            @Override
//            public byte[] getBytes() throws IOException {
//                return data;
//            }
//
//            @Override
//            public InputStream getInputStream() throws IOException {
//                return new ByteArrayInputStream(data);
//            }
//
//            @Override
//            public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
//                Files.write(dest.toPath(), data);
//            }
//        };
//    }
//}
