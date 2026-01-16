package com.file.project.dto;

import com.file.project.enums.FileCategory;
import lombok.Data;

@Data
public class FileUpdateRequestDTO {
    private String name;
    private String description;
    private FileCategory category;
    private Integer retentionPeriod;
}
