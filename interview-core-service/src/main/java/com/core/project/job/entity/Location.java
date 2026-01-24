//package com.core.project.job.entity;//package com.itsutra.project.job.entity;
//
//import jakarta.validation.constraints.NotBlank;
//import lombok.*;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.relational.core.mapping.Table;
//
//import java.time.LocalDateTime;
//
//@Table(name = "locations")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Location {
//
//    @Id
//    private Long id;
//
//    @NotBlank
//    private String name;
//
//    private String code;
//
//    private String address;
//
//
//    private String city;
//
//    private String state;
//
//    private String country;
//
//
//    private String postalCode;
//
//
//    private String timezone;
//
//    @Builder.Default
//    private Boolean isActive = true;
//
//    @Builder.Default
//    private Boolean isRemote = false;
//
//    private String contactPerson;
//
//    private String contactEmail;
//
//    private String contactPhone;
//
//    private String facilities;
//
//    private LocalDateTime createdAt;
//
//    private LocalDateTime updatedAt;
//
////    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
////    @Builder.Default
////    private List<JobPosition> jobPositions = new ArrayList<>();
//
//    // Helper methods
//    public String getFullAddress() {
//        return String.format("%s, %s, %s %s, %s",
//                address, city, state, postalCode, country);
//    }
//
//    public Boolean isPhysicalLocation() {
//        return !isRemote;
//    }
//}
