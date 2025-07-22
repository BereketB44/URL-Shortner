package com.porchpick.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YardSaleResponseDto {
    private UUID id;
    private String title;
    private String description;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private List<ItemResponseDto> items;
} 