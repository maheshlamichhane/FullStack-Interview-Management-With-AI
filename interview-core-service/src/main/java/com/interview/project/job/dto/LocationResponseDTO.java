package com.interview.project.job.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LocationResponseDTO {
    private Long id;
    private String name;
    private String code;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String timezone;
    private String fullAddress;
    private Boolean isActive;
    private Boolean isRemote;
    private String contactPerson;
    private String contactEmail;
    private String contactPhone;
    private List<String> facilities;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer jobPositionCount;
}
