package com.itsutra.project.service;


import com.itsutra.project.dao.DocumentDAO;
import com.itsutra.project.dao.FileDAO;
import com.itsutra.project.dao.UserDAO;
import com.itsutra.project.dto.DocumentCreateRequestDTO;
import com.itsutra.project.dto.DocumentResponseDTO;
import com.itsutra.project.dto.DocumentUpdateRequestDTO;
import com.itsutra.project.dto.DocumentVersionRequestDTO;
import com.itsutra.project.entity.Document;
import com.itsutra.project.entity.File;
import com.itsutra.project.entity.User;
import com.itsutra.project.enums.AccessType;
import com.itsutra.project.enums.DocumentCategory;
import com.itsutra.project.enums.DocumentStatus;
import com.itsutra.project.mapper.FileStorageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DocumentService {

    private final DocumentDAO documentDAO;
    private final FileDAO fileDAO;
    private final UserDAO userDAO;
    private final FileStorageMapper fileStorageMapper;
    private final DocumentAccessService documentAccessService;

    // Create Document
    public DocumentResponseDTO createDocument(DocumentCreateRequestDTO request) {
        log.info("Creating document: {}", request.getTitle());

        // Validate file exists
        File file = fileDAO.findById(request.getFileId())
                .orElseThrow(() -> new IllegalArgumentException("File not found with id: " + request.getFileId()));

        // Get user who created the document
        User createdBy = null;
        if (request.getCreatedById() != null) {
            createdBy = userDAO.findById(request.getCreatedById())
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + request.getCreatedById()));
        }

        Document document = fileStorageMapper.toDocumentEntity(request, file, createdBy);
        Document savedDocument = documentDAO.save(document);

        log.info("Successfully created document with id: {}", savedDocument.getId());
        return fileStorageMapper.toDocumentResponse(savedDocument);
    }

    // Get Document by ID
    @Transactional(readOnly = true)
    public DocumentResponseDTO getDocumentById(Long id) {
        log.debug("Fetching document by id: {}", id);
        Document document = documentDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Document not found with id: " + id));

        // Log access
        documentAccessService.logDocumentAccess(document, AccessType.VIEW);

        return fileStorageMapper.toDocumentResponse(document);
    }

    // Get All Documents
    @Transactional(readOnly = true)
    public Page<DocumentResponseDTO> getAllDocuments(Pageable pageable) {
        log.debug("Fetching all documents");
        return documentDAO.findAll(pageable)
                .map(fileStorageMapper::toDocumentResponse);
    }

    // Update Document
    public DocumentResponseDTO updateDocument(Long id, DocumentUpdateRequestDTO request) {
        log.info("Updating document with id: {}", id);

        Document document = documentDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Document not found with id: " + id));

        Optional.ofNullable(request.getTitle()).ifPresent(document::setTitle);
        Optional.ofNullable(request.getDescription()).ifPresent(document::setDescription);
        Optional.ofNullable(request.getDocumentType()).ifPresent(document::setDocumentType);
        Optional.ofNullable(request.getCategory()).ifPresent(document::setCategory);
        Optional.ofNullable(request.getTags()).ifPresent(tags ->
                document.setTags(fileStorageMapper.convertListToString(tags)));
        Optional.ofNullable(request.getMetadata()).ifPresent(metadata ->
                document.setMetadata(fileStorageMapper.convertToJson(metadata)));
        Optional.ofNullable(request.getIsConfidential()).ifPresent(document::setIsConfidential);
        Optional.ofNullable(request.getStatus()).ifPresent(document::setStatus);

        Document updatedDocument = documentDAO.save(document);
        log.info("Successfully updated document with id: {}", id);
        return fileStorageMapper.toDocumentResponse(updatedDocument);
    }

    // Delete Document
    public void deleteDocument(Long id) {
        log.info("Deleting document with id: {}", id);
        Document document = documentDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Document not found with id: " + id));

        document.setStatus(DocumentStatus.ARCHIVED);
        documentDAO.save(document);
        log.info("Successfully archived document with id: {}", id);
    }

    // Create Document Version
    public DocumentResponseDTO createDocumentVersion(Long documentId, DocumentVersionRequestDTO request) {
        log.info("Creating new version for document id: {}", documentId);

        Document originalDocument = documentDAO.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Document not found with id: " + documentId));

        File newFile = fileDAO.findById(request.getFileId())
                .orElseThrow(() -> new IllegalArgumentException("File not found with id: " + request.getFileId()));

        // Create new version
        Document newVersion = Document.builder()
                .title(originalDocument.getTitle())
                .description(request.getDescription() != null ? request.getDescription() : originalDocument.getDescription())
                .documentType(originalDocument.getDocumentType())
                .category(originalDocument.getCategory())
                .tags(originalDocument.getTags())
                .metadata(request.getMetadata() != null ?
                        fileStorageMapper.convertToJson(request.getMetadata()) : originalDocument.getMetadata())
                .isConfidential(originalDocument.getIsConfidential())
                .isVerified(false)
                .version(originalDocument.getVersion() + 1)
                .parentDocumentId(originalDocument.getId())
                .status(DocumentStatus.DRAFT)
                .file(newFile)
                .createdBy(originalDocument.getCreatedBy())
                .build();

        Document savedVersion = documentDAO.save(newVersion);
        log.info("Successfully created document version with id: {}", savedVersion.getId());
        return fileStorageMapper.toDocumentResponse(savedVersion);
    }

    // Get Document Versions
    @Transactional(readOnly = true)
    public List<DocumentResponseDTO> getDocumentVersions(Long documentId) {
        log.debug("Fetching versions for document id: {}", documentId);
        List<Document> versions = documentDAO.findVersionsByParentId(documentId);
        return versions.stream()
                .map(fileStorageMapper::toDocumentResponse)
                .collect(Collectors.toList());
    }

    // Verify Document
    public DocumentResponseDTO verifyDocument(Long id, Long verifiedByUserId) {
        log.info("Verifying document with id: {} by user: {}", id, verifiedByUserId);

        Document document = documentDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Document not found with id: " + id));

        User verifiedBy = userDAO.findById(verifiedByUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + verifiedByUserId));

        document.setIsVerified(true);
        document.setVerifiedBy(verifiedBy.getId());
        document.setVerifiedAt(LocalDateTime.now());
        document.setStatus(DocumentStatus.APPROVED);

        Document verifiedDocument = documentDAO.save(document);
        log.info("Successfully verified document with id: {}", id);
        return fileStorageMapper.toDocumentResponse(verifiedDocument);
    }

    // Search Documents by Tag
    @Transactional(readOnly = true)
    public Page<DocumentResponseDTO> searchDocumentsByTag(String tag, Pageable pageable) {
        log.debug("Searching documents by tag: {}", tag);
        Page<Document> documents = documentDAO.findByTag(tag, pageable);
        return documents.map(fileStorageMapper::toDocumentResponse);
    }

    // Get Documents by Category
    @Transactional(readOnly = true)
    public Page<DocumentResponseDTO> getDocumentsByCategory(DocumentCategory category, Pageable pageable) {
        log.debug("Fetching documents by category: {}", category);
        Page<Document> documents = documentDAO.findByCategory(category, pageable);
        return documents.map(fileStorageMapper::toDocumentResponse);
    }

    // Get Document Statistics
    @Transactional(readOnly = true)
    public Map<String, Object> getDocumentStatistics() {
        log.debug("Fetching document statistics");

        Long totalDocuments = documentDAO.count();
        Long verifiedDocuments = documentDAO.findVerifiedDocuments(Pageable.unpaged()).getTotalElements();
        List<Object[]> categoryCounts = documentDAO.countDocumentsByCategory();

        Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("totalDocuments", totalDocuments);
        stats.put("verifiedDocuments", verifiedDocuments);
        stats.put("categoryDistribution", categoryCounts.stream()
                .collect(java.util.stream.Collectors.toMap(
                        arr -> arr[0].toString(),
                        arr -> arr[1]
                )));

        return stats;
    }
}
