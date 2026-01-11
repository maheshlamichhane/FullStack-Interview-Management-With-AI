package com.interview.project.file.dto;

import com.interview.project.file.enums.FileCategory;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileSearchRequestDTO {
    private String name;
    private FileCategory category;
    private String mimeType;
    private Long minSize;
    private Long maxSize;
    private LocalDateTime uploadedAfter;
    private LocalDateTime uploadedBefore;
    private Long uploadedById;
}
