package com.core.project.job.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class DepartmentNotFound {

    private Integer id;
    private final String message = "department not found";

}
