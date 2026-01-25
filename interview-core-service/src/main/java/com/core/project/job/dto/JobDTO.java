package com.core.project.job.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JobDTO {

    private Long id;
    private String name;
    private Long departmentId;
}
