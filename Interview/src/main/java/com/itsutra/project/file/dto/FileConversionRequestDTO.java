package com.itsutra.project.file.dto;

import lombok.Data;

import java.util.Map;

@Data
public class FileConversionRequestDTO {
    private String targetFormat;
    private Integer quality;
    private Integer width;
    private Integer height;
    private Boolean preserveMetadata;
    private Map<String, Object> conversionOptions;
}
