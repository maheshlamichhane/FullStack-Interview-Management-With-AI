package com.core.project.candidate.entity;


import com.core.project.candidate.enums.ParsingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;


@Table(name = "resumes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resume {

    @Id
    private Long id;


    private String fileName;

    private String filePath;

    private Long fileSize;

    private String fileType;

    @Builder.Default
    private Integer version = 1;

    @Builder.Default
    private Boolean isPrimary = false;

    private String parsedContent;

    @Builder.Default
    private ParsingStatus parsingStatus = ParsingStatus.PENDING;


    private String parsedSkills;

    private Double parsedExperience;

    private LocalDateTime uploadedAt;

}