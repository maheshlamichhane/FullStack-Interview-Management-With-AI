package com.core.project.job.entity;//package com.itsutra.project.job.entity;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotBlank;
//import lombok.*;
//import org.hibernate.annotations.CreationTimestamp;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Table(name = "locations")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Location {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @NotBlank
//    @Column(name = "name", nullable = false)
//    private String name;
//
//    @Column(name = "code", unique = true)
//    private String code; // Location code like "NYC", "LON"
//
//    @Column(name = "address")
//    private String address;
//
//    @Column(name = "city")
//    private String city;
//
//    @Column(name = "state")
//    private String state;
//
//    @Column(name = "country", nullable = false)
//    private String country;
//
//    @Column(name = "postal_code")
//    private String postalCode;
//
//    @Column(name = "timezone")
//    private String timezone;
//
//    @Column(name = "is_active")
//    @Builder.Default
//    private Boolean isActive = true;
//
//    @Column(name = "is_remote")
//    @Builder.Default
//    private Boolean isRemote = false;
//
//    @Column(name = "contact_person")
//    private String contactPerson;
//
//    @Column(name = "contact_email")
//    private String contactEmail;
//
//    @Column(name = "contact_phone")
//    private String contactPhone;
//
//    @Column(name = "facilities", columnDefinition = "TEXT")
//    private String facilities;
//
//    @CreationTimestamp
//    @Column(name = "created_at", updatable = false)
//    private LocalDateTime createdAt;
//
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
//
//    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
//    @Builder.Default
//    private List<JobPosition> jobPositions = new ArrayList<>();
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
