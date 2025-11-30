package com.itsutra.project.dto;

import lombok.Data;

import java.util.List;

@Data
public class DepartmentTreeResponseDTO {
    private Long id;
    private String name;
    private String code;
    private List<DepartmentTreeResponseDTO> children;
    private Integer totalPositions;
    private Integer openPositions;
}
