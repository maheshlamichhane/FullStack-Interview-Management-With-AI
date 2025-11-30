package com.itsutra.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ChunkUploadRequestDTO {
    @NotBlank(message = "Session ID is required")
    private String sessionId;

    @NotNull(message = "Chunk number is required")
    private Integer chunkNumber;

    private MultipartFile chunk;
    private String checksum;
}
