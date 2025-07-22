package com.porchpick.app.service;

import com.porchpick.app.dto.ItemRequestDto;
import com.porchpick.app.dto.ItemResponseDto;

import java.util.List;
import java.util.UUID;

public interface IItemService {
    ItemResponseDto createItem(ItemRequestDto itemRequestDto);
    List<ItemResponseDto> getAllItemsByUserId(UUID userId);

    List<ItemResponseDto> getAllItems();
    ItemResponseDto getItemById(UUID itemId);
    ItemResponseDto updateItem(UUID itemId, ItemRequestDto itemRequestDto);
    void deleteItem(UUID itemId);
} 