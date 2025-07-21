package com.porchpick.app.dto;

import jakarta.validation.constraints.Email;
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
public class AuthRequestDto {
    @Email
    @NotBlank
    @Size(max = 254)
    private String email;

    @NotBlank
    @Size(min = 8, max = 255)
    private String password;
} 