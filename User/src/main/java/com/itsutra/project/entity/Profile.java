package com.itsutra.project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // Personal Information
    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @Column(name = "bio", length = 500)
    private String bio;

    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;

    @Column(name = "gender")
    private String gender;

    // Professional Information
    @Column(name = "company")
    private String company;

    @Column(name = "department")
    private String department;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "employee_id")
    private String employeeId;

    @Column(name = "hire_date")
    private LocalDateTime hireDate;

    // Contact Information
    @Column(name = "work_phone")
    private String workPhone;

    @Column(name = "mobile_phone")
    private String mobilePhone;

    @Column(name = "office_location")
    private String officeLocation;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @Column(name = "postal_code")
    private String postalCode;

    // Preferences & Settings
    @Column(name = "timezone")
    private String timezone;

    @Column(name = "language")
    private String language;

    @Column(name = "notification_preferences", columnDefinition = "TEXT")
    private String notificationPreferences; // JSON: {"email": true, "sms": false, "push": true}

    @Column(name = "display_preferences", columnDefinition = "TEXT")
    private String displayPreferences; // JSON: {"theme": "dark", "density": "compact"}

    // Professional Details
    @Column(name = "skills", columnDefinition = "TEXT")
    private String skills; // JSON array: ["Java", "Spring Boot", "Microservices"]

    @Column(name = "certifications", columnDefinition = "TEXT")
    private String certifications; // JSON array

    @Column(name = "education", columnDefinition = "TEXT")
    private String education; // JSON array

    @Column(name = "work_experience", columnDefinition = "TEXT")
    private String workExperience; // JSON array

    // System Fields
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
