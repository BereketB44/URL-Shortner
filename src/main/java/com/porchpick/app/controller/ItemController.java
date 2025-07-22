package com.porchpick.app.controller;

import com.porchpick.app.dto.ItemRequestDto;
import com.porchpick.app.dto.ItemResponseDto;
import com.porchpick.app.service.IItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/items")
@CrossOrigin("*")
public class ItemController {

    @Autowired
    private IItemService itemService;

    @PostMapping
    public ResponseEntity<ItemResponseDto> createItem(@Valid @RequestBody ItemRequestDto itemRequestDto) {
        ItemResponseDto createdItem = itemService.createItem(itemRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

    @GetMapping
    public ResponseEntity<List<ItemResponseDto>> getItems(@RequestParam(required = false) UUID userId) {
        if (userId != null) {
            return ResponseEntity.ok(itemService.getAllItemsByUserId(userId));
        } else {
            return ResponseEntity.ok(itemService.getAllItems());
        }
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponseDto> getItemById(@PathVariable UUID itemId) {
        ItemResponseDto item = itemService.getItemById(itemId);
        return ResponseEntity.ok(item);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<ItemResponseDto> updateItem(@PathVariable UUID itemId, @Valid @RequestBody ItemRequestDto itemRequestDto) {
        ItemResponseDto updatedItem = itemService.updateItem(itemId, itemRequestDto);
        return ResponseEntity.accepted().body(updatedItem);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable UUID itemId) {
        itemService.deleteItem(itemId);
        return ResponseEntity.noContent().build();
    }
}
