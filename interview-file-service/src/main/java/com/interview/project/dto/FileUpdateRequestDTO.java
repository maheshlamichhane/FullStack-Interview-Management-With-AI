package com.interview.project.dto;

import com.interview.project.enums.FileCategory;
import lombok.Data;

@Data
public class FileUpdateRequestDTO {
    private String name;
    private String description;
    private FileCategory category;
    private Integer retentionPeriod;
}
