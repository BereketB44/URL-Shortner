package com.porchpick.app.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YardSaleRequestDto {

    @NotNull
    private UUID userId;

    @NotBlank
    private String title;

    private String description;

    @NotNull
    @Future
    private OffsetDateTime startTime;

    @NotNull
    @Future
    private OffsetDateTime endTime;
    
    private Set<UUID> itemIds;
} 