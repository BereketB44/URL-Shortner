package com.porchpick.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id; // This will now be populated by the User's ID

    @OneToOne
    @MapsId // This is the key change: it maps the 'id' field to the User's primary key
    @JoinColumn(name = "id") // The PK of this table ('profiles') is also the FK to the 'users' table
    private User user;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false)
    private String street;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String city;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String state;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String country;

    @NotBlank
    @Size(max = 20)
    @Column(name = "zip_code", nullable = false, length = 20)
    private String zipCode;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
} 