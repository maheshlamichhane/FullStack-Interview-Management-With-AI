package com.itsutra.project.notification.service;


import com.itsutra.project.common.service.AuthenticationService;
import com.itsutra.project.notification.dao.NotificationHistoryRepository;
import com.itsutra.project.notification.dao.NotificationTemplateRepository;
import com.itsutra.project.notification.dto.NotificationRequest;
import com.itsutra.project.notification.mapper.NotificationMapper;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class EmailNotificationServiceImpl extends AbstractNotificationService {

    @Autowired
    private JavaMailSender mailSender;


    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailNotificationServiceImpl(NotificationMapper notificationMapper, NotificationHistoryRepository notificationHistoryRepository, NotificationTemplateRepository notificationTemplateRepository, AuthenticationService authenticationService, JavaMailSender mailSender, AuthenticationService authenticationService1, NotificationTemplateRepository notificationTemplateRepository1, NotificationHistoryRepository notificationHistoryRepository1, NotificationMapper notificationMapper1) {
        super(notificationMapper, notificationHistoryRepository, notificationTemplateRepository, authenticationService);
        this.mailSender = mailSender;
    }

    @Override
    public String sendNotification(NotificationRequest emailNotificationRequest, String body) {

        // Process based on notification type
        String providerResponse;

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(emailNotificationRequest.getRecipient());
            helper.setSubject(emailNotificationRequest.getSubject());
            helper.setText(body, true);

            mailSender.send(message);

            log.info("Email sent successfully to: {}", emailNotificationRequest.getRecipient());
            return "SUCCESS";

        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", emailNotificationRequest.getRecipient(), e.getMessage(), e);
            return "FAILED: " + e.getMessage();
        }
    }

    private String generateReferenceId() {
        return "NOTIF_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }

//    @Data
//    @Builder
//    public class UniversalAttachment {
//        private AttachmentSource sourceType;  // What type of attachment
//
//        // Common fields for all types
//        private String fileName;     // "report.pdf"
//        private String contentType;  // "application/pdf" (optional)
//
//        // ONLY ONE of these is populated based on sourceType:
//        private String filePath;        // For FILE_PATH
//        private byte[] fileBytes;       // For BYTE_ARRAY
//        private String base64Content;   // For BASE64
//        private MultipartFile multipartFile; // For MULTIPART_FILE
//        private InputStream inputStream;// For INPUT_STREAM
//        private String url;             // For URL
//    }

//    private void addUniversalAttachment(MimeMessageHelper helper, UniversalAttachment attachment)
//            throws MessagingException, IOException {
//
//        // Determine attachment source and add accordingly
//        switch (attachment.getSourceType()) {
//            case FILE_PATH:
//                addFileAttachment(helper, attachment);
//                break;
//
//            case BYTE_ARRAY:
//                addByteArrayAttachment(helper, attachment);
//                break;
//
//            case BASE64:
//                addBase64Attachment(helper, attachment);
//                break;
//
//            case MULTIPART_FILE:
//                addMultipartFileAttachment(helper, attachment);
//                break;
//
//            case INPUT_STREAM:
//                addInputStreamAttachment(helper, attachment);
//                break;
//
//            case URL:
//                addUrlAttachment(helper, attachment);
//                break;
//
//            default:
//                throw new IllegalArgumentException("Unsupported attachment type: " +
//                        attachment.getSourceType());
//        }
//
//        private void addFileAttachment(MimeMessageHelper helper, UniversalAttachment attachment)
//            throws MessagingException, IOException {
//
//            File file = new File(attachment.getFilePath());
//            validateFileExists(file);
//            validateFileSize(file.length());
//
//            FileSystemResource resource = new FileSystemResource(file);
//            helper.addAttachment(
//                    getFileName(attachment, file.getName()),
//                    resource,
//                    getContentType(attachment, Files.probeContentType(file.toPath()))
//            );
//        }
//
//        private void addBase64Attachment(MimeMessageHelper helper, UniversalAttachment attachment)
//            throws MessagingException {
//
//            byte[] decodedBytes = Base64.getDecoder().decode(attachment.getBase64Content());
//            validateByteArray(decodedBytes);
//
//            ByteArrayResource resource = new ByteArrayResource(decodedBytes);
//            helper.addAttachment(
//                    attachment.getFileName(),
//                    resource,
//                    getContentType(attachment, null)
//            );
//        }
//
//        private void addMultipartFileAttachment(MimeMessageHelper helper, UniversalAttachment attachment)
//            throws MessagingException, IOException {
//
//            MultipartFile multipartFile = attachment.getMultipartFile();
//            validateMultipartFile(multipartFile);
//
//            helper.addAttachment(
//                    attachment.getFileName() != null ? attachment.getFileName() : multipartFile.getOriginalFilename(),
//                    () -> multipartFile.getInputStream(),
//                    getContentType(attachment, multipartFile.getContentType())
//            );
//        }
//        private void addInputStreamAttachment(MimeMessageHelper helper, UniversalAttachment attachment)
//            throws MessagingException, IOException {
//
//            try (InputStream inputStream = attachment.getInputStream()) {
//                byte[] bytes = inputStream.readAllBytes();
//                validateByteArray(bytes);
//
//                ByteArrayResource resource = new ByteArrayResource(bytes);
//                helper.addAttachment(
//                        attachment.getFileName(),
//                        resource,
//                        getContentType(attachment, null)
//                );
//            }
//        }
//
//        private void addUrlAttachment(MimeMessageHelper helper, UniversalAttachment attachment)
//            throws MessagingException, IOException {
//
//            try (InputStream inputStream = new URL(attachment.getUrl()).openStream()) {
//                byte[] bytes = inputStream.readAllBytes();
//                validateByteArray(bytes);
//
//                ByteArrayResource resource = new ByteArrayResource(bytes);
//                helper.addAttachment(
//                        attachment.getFileName(),
//                        resource,
//                        getContentType(attachment, null)
//                );
//            }
//        }
//
//        private String getFileName(UniversalAttachment attachment, String defaultName) {
//            return attachment.getFileName() != null ? attachment.getFileName() : defaultName;
//        }
//
//        private String getContentType(UniversalAttachment attachment, String detectedType) {
//            return attachment.getContentType() != null ?
//                    attachment.getContentType() :
//                    (detectedType != null ? detectedType : "application/octet-stream");
//        }
//
//        private void validateFileExists(File file) throws IOException {
//            if (!file.exists()) {
//                throw new IOException("File not found: " + file.getAbsolutePath());
//            }
//            if (!file.canRead()) {
//                throw new IOException("Cannot read file: " + file.getAbsolutePath());
//            }
//        }
//
//        private void validateFileSize(long size) throws IOException {
//            if (size > maxAttachmentSize) {
//                throw new IOException(String.format(
//                        "File size %d bytes exceeds maximum allowed size %d bytes",
//                        size, maxAttachmentSize));
//            }
//        }
//
//        private void validateByteArray(byte[] bytes) throws IOException {
//            if (bytes == null || bytes.length == 0) {
//                throw new IOException("Byte array is empty or null");
//            }
//            validateFileSize(bytes.length);
//        }
//
//        private void validateMultipartFile(MultipartFile file) throws IOException {
//            if (file == null || file.isEmpty()) {
//                throw new IOException("MultipartFile is empty or null");
//            }
//            validateFileSize(file.getSize());
//        }


    }
