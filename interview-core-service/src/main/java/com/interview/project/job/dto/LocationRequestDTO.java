package com.interview.project.job.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class LocationRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    private String code;
    private String address;
    private String city;
    private String state;

    @NotBlank(message = "Country is required")
    private String country;

    private String postalCode;
    private String timezone;
    private String contactPerson;
    private String contactEmail;
    private String contactPhone;
    private boolean remote;
    private List<String> facilities;

    public boolean isRemote() {
        return remote;
    }
}
