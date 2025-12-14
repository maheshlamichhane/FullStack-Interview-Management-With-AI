package com.itsutra.project.dto;//package com.itsutra.project.dto;
//
//import lombok.Builder;
//import lombok.Data;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.InputStream;
//
//@Data
//@Builder
//public class UniversalAttachment {
//    private AttachmentSource sourceType;
//
//    // Common fields for all types
//    private String fileName;
//    private String contentType;
//
//    // ONLY ONE of these is populated based on sourceType:
//    private String filePath;
//    private byte[] fileBytes;
//    private String base64Content;
//    private MultipartFile multipartFile;
//    private InputStream inputStream;
//    private String url;
//}
