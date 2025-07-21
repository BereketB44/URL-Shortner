package com.porchpick.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileResponseDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String bio;
    private String street;
    private String city;
    private String state;
    private String country;
    private String zipCode;
} 