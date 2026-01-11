package com.interview.project.file.dto;

import com.interview.project.file.enums.FileCategory;
import lombok.Data;

@Data
public class FileUpdateRequestDTO {
    private String name;
    private String description;
    private FileCategory category;
    private Integer retentionPeriod;
}
