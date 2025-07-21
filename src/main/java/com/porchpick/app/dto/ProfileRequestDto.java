package com.porchpick.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileRequestDto {
    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    private String bio;

    @NotBlank
    @Size(max = 255)
    private String street;

    @NotBlank
    @Size(max = 100)
    private String city;

    @NotBlank
    @Size(max = 100)
    private String state;

    @NotBlank
    @Size(max = 100)
    private String country;

    @NotBlank
    @Size(max = 20)
    private String zipCode;
} 