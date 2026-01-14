package com.interview.project.dto;

import com.interview.project.enums.FileCategory;
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
