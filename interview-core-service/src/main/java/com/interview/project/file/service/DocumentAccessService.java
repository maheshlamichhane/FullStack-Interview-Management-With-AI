package com.interview.project.file.service;//package com.itsutra.project.file.service;
//
//import com.itsutra.project.file.dao.DocumentAccessLogDAO;
//import com.itsutra.project.file.entity.Document;
//import com.itsutra.project.file.entity.DocumentAccessLog;
//import com.itsutra.project.file.enums.AccessType;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class DocumentAccessService {
//
//    private final DocumentAccessLogDAO documentAccessLogDAO;
//
//    @Transactional
//    public void logDocumentAccess(Document document, AccessType accessType, String remoteAddr, String userAgent, String referrer) {
//            try {
//                DocumentAccessLog accessLog = DocumentAccessLog.builder()
//                        .document(document)
//                        .accessType(accessType)
//                        .ipAddress(remoteAddr)
//                        .userAgent(userAgent)
//                        .referrer(referrer)
//                        .accessedAt(LocalDateTime.now())
//                        .accessedBy(document.getCreatedBy().getId())
//                        .build();
//
//                documentAccessLogDAO.save(accessLog);
//                log.debug("Logged {} access for document: {}", accessType, document.getId());
//            } catch (Exception e) {
//                log.error("Error logging document access for document: {}", document.getId(), e);
//            }
//    }
//
////    public void logDocumentAccess(Document document, AccessType accessType,
////                                  HttpServletRequest request) {
////        String ipAddress = getClientIpAddress(request);
////        String userAgent = request.getHeader("User-Agent");
////        String referrer = request.getHeader("Referer");
////
////        logDocumentAccess(document, accessType, ipAddress, userAgent, referrer);
////    }
//
////    public void logDocumentAccess(Document document, AccessType accessType,
////                                  String ipAddress, String userAgent) {
////        logDocumentAccess(document, accessType, ipAddress, userAgent, null);
////    }
//
////    public void logDocumentAccess(Document document, AccessType accessType,
////                                  String ipAddress, String userAgent, String referrer) {
////        try {
////            DocumentAccessLog accessLog = DocumentAccessLog.builder()
////                    .document(document)
////                    .accessType(accessType)
////                    .ipAddress(ipAddress)
////                    .userAgent(userAgent)
////                    .referrer(referrer)
////                    .accessedAt(LocalDateTime.now())
////                    .build();
////
////            documentAccessLogDAO.save(accessLog);
////            log.debug("Logged {} access for document: {}", accessType, document.getId());
////        } catch (Exception e) {
////            log.error("Error logging document access for document: {}", document.getId(), e);
////        }
////    }
//
//    private String getClientIpAddress(HttpServletRequest request) {
//        String xForwardedFor = request.getHeader("X-Forwarded-For");
//        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
//            return xForwardedFor.split(",")[0].trim();
//        }
//        return request.getRemoteAddr();
//    }
//}
