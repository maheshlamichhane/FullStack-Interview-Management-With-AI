package com.itsutra.project.file.service;



import com.itsutra.project.common.dao.UserDAO;
import com.itsutra.project.common.entity.User;
import com.itsutra.project.common.service.AuthenticationService;
import com.itsutra.project.file.dao.DocumentDAO;
import com.itsutra.project.file.dao.FileDAO;
import com.itsutra.project.file.dto.DocumentCreateRequestDTO;
import com.itsutra.project.file.dto.DocumentResponseDTO;
import com.itsutra.project.file.dto.DocumentUpdateRequestDTO;
import com.itsutra.project.file.dto.DocumentVersionRequestDTO;
import com.itsutra.project.file.entity.Document;
import com.itsutra.project.file.entity.File;
import com.itsutra.project.file.enums.AccessType;
import com.itsutra.project.file.enums.DocumentCategory;
import com.itsutra.project.file.enums.DocumentStatus;
import com.itsutra.project.file.mapper.FileStorageMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final AuthenticationService authenticationService;

    @Transactional
    public DocumentResponseDTO createDocument(DocumentCreateRequestDTO request) {
        log.info("Creating document: {}", request.getTitle());

        User user = authenticationService.getCurrentUser();

        // Validate file exists
        File file = fileDAO.findByIdAndCreatedById(request.getFileId(),user.getId())
                .orElseThrow(() -> new IllegalArgumentException("File not found with id: " + request.getFileId()));

        Document document = fileStorageMapper.toDocumentEntity(request, file, user);
        Document savedDocument = documentDAO.save(document);

        log.info("Successfully created document with id: {}", savedDocument.getId());
        return fileStorageMapper.toDocumentResponse(savedDocument);
    }


    @Transactional(readOnly = true)
    public List<DocumentResponseDTO> getAllDocuments() {
        log.debug("Fetching all documents");
        User user = authenticationService.getCurrentUser();
        return documentDAO.findByCreatedById(user.getId()).stream()
                .map(fileStorageMapper::toDocumentResponse).toList();
    }


    @Transactional
    public DocumentResponseDTO getDocumentById(Long id, HttpServletRequest request) {
        log.debug("Fetching document by id: {}", id);
        User user = authenticationService.getCurrentUser();
        Document document = documentDAO.findByIdAndCreatedById(id,user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Document not found with id: " + id));

        // Log access
        String userAgent = request.getHeader("User-Agent");
        String referrer = request.getHeader("Referer");
        String remoteAddress = request.getRemoteAddr();
        documentAccessService.logDocumentAccess(document, AccessType.VIEW,remoteAddress,userAgent,referrer);
        return fileStorageMapper.toDocumentResponse(document);
    }




    @Transactional
    public DocumentResponseDTO updateDocument(Long id, DocumentUpdateRequestDTO request, HttpServletRequest httpServletRequest) {
        log.info("Updating document with id: {}", id);

        User user = authenticationService.getCurrentUser();
        Document document = documentDAO.findByIdAndCreatedById(id,user.getId())
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

        //Access Logs
        String userAgent = httpServletRequest.getHeader("User-Agent");
        String referrer = httpServletRequest.getHeader("Referer");
        String remoteAddress = httpServletRequest.getRemoteAddr();
        documentAccessService.logDocumentAccess(document, AccessType.EDIT,remoteAddress,userAgent,referrer);

        Document updatedDocument = documentDAO.save(document);
        log.info("Successfully updated document with id: {}", id);
        return fileStorageMapper.toDocumentResponse(updatedDocument);
    }






    @Transactional
    public DocumentResponseDTO createDocumentVersion(Long documentId, DocumentVersionRequestDTO request) {
        log.info("Creating new version for document id: {}", documentId);

        User user = authenticationService.getCurrentUser();
        Document originalDocument = documentDAO.findByIdAndCreatedById(documentId,user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Document not found with id: " + documentId));

        File newFile = fileDAO.findByIdAndCreatedById(request.getFileId(),user.getId())
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




    @Transactional(readOnly = true)
    public List<DocumentResponseDTO> getDocumentVersions(Long documentId) {

        log.debug("Fetching versions for document id: {}", documentId);
        User user = authenticationService.getCurrentUser();
        List<Document> versions = documentDAO.findByParentDocumentIdAndCreatedById(documentId,user.getId());
        return versions.stream()
                .map(fileStorageMapper::toDocumentResponse)
                .collect(Collectors.toList());
    }



    @Transactional
    public DocumentResponseDTO verifyDocument(Long id) {

        User user = authenticationService.getCurrentUser();
        Document document = documentDAO.findByIdAndCreatedById(id,user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Document not found with id: " + id));

        document.setIsVerified(true);
        document.setVerifiedBy(user.getId());
        document.setVerifiedAt(LocalDateTime.now());
        document.setStatus(DocumentStatus.APPROVED);

        Document verifiedDocument = documentDAO.save(document);
        log.info("Successfully verified document with id: {}", id);
        return fileStorageMapper.toDocumentResponse(verifiedDocument);
    }




    @Transactional(readOnly = true)
    public List<DocumentResponseDTO> searchDocumentsByTag(String tag) {
        User user = authenticationService.getCurrentUser();
        List<Document> documents = documentDAO.getByTagAndCreatedIdInfo(tag, user.getId());

        return documents.stream()
                .map(fileStorageMapper::toDocumentResponse)
                .toList();
    }






    @Transactional(readOnly = true)
    public List<DocumentResponseDTO> getDocumentsByCategory(DocumentCategory category) {
        log.debug("Fetching documents by category: {}", category);
        List<Document> documents = documentDAO.findByCategory(category);
        return documents.stream().map(fileStorageMapper::toDocumentResponse).toList();
    }



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
                .collect(Collectors.toMap(
                        arr -> arr[0].toString(),
                        arr -> arr[1]
                )));

        return stats;
    }


    // Delete Document
    public void deleteDocument(Long id) {
        log.info("Deleting document with id: {}", id);
        User user = authenticationService.getCurrentUser();
        Document document = documentDAO.findByIdAndCreatedById(id,user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Document not found with id: " + id));

        document.setStatus(DocumentStatus.ARCHIVED);
        documentDAO.save(document);
        log.info("Successfully archived document with id: {}", id);
    }
}
