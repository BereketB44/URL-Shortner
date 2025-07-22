package com.porchpick.app.service;

import com.porchpick.app.dto.YardSaleRequestDto;
import com.porchpick.app.dto.YardSaleResponseDto;

import java.util.List;
import java.util.UUID;

public interface IYardSaleService {
    YardSaleResponseDto createYardSale(YardSaleRequestDto yardSaleRequestDto);
    List<YardSaleResponseDto> getAllYardSalesByUserId(UUID userId);
    List<YardSaleResponseDto> getAllYardSales();
    YardSaleResponseDto getYardSaleById(UUID yardSaleId);
    YardSaleResponseDto updateYardSale(UUID yardSaleId, YardSaleRequestDto yardSaleRequestDto);
    void deleteYardSale(UUID userId, UUID yardSaleId);
    YardSaleResponseDto addItemToYardSale(UUID userId, UUID yardSaleId, UUID itemId);
    YardSaleResponseDto removeItemFromYardSale(UUID userId, UUID yardSaleId, UUID itemId);
} 