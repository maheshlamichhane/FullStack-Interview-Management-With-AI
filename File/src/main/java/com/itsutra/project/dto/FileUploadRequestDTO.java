package com.itsutra.project.dto;

import com.itsutra.project.enums.FileCategory;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileUploadRequestDTO {
    @NotNull(message = "File is required")
    private MultipartFile file;

    private String description;
    private FileCategory category;
    private Boolean isEncrypted;
    private Integer retentionPeriod;
    private Long uploadedById;
}
