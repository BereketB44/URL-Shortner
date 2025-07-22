package com.porchpick.app.controller;

import com.porchpick.app.dto.YardSaleRequestDto;
import com.porchpick.app.dto.YardSaleResponseDto;
import com.porchpick.app.service.IYardSaleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/yardsales")
@CrossOrigin("*")
public class YardSaleController {

    @Autowired
    private IYardSaleService yardSaleService;

    @PostMapping
    public ResponseEntity<YardSaleResponseDto> createYardSale(@Valid @RequestBody YardSaleRequestDto yardSaleRequestDto) {
        YardSaleResponseDto createdYardSale = yardSaleService.createYardSale(yardSaleRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdYardSale);
    }

    @GetMapping
    public ResponseEntity<List<YardSaleResponseDto>> getAllYardSales() {
        List<YardSaleResponseDto> yardSales = yardSaleService.getAllYardSales();
        return ResponseEntity.ok(yardSales);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<YardSaleResponseDto>> getAllYardSalesByUserId(@PathVariable UUID userId) {
        List<YardSaleResponseDto> yardSales = yardSaleService.getAllYardSalesByUserId(userId);
        return ResponseEntity.ok(yardSales);
    }

    @GetMapping("/{yardSaleId}")
    public ResponseEntity<YardSaleResponseDto> getYardSaleById(@PathVariable UUID yardSaleId) {
        YardSaleResponseDto yardSale = yardSaleService.getYardSaleById(yardSaleId);
        return ResponseEntity.ok(yardSale);
    }

    @PutMapping("/{yardSaleId}")
    public ResponseEntity<YardSaleResponseDto> updateYardSale(@PathVariable UUID yardSaleId, @Valid @RequestBody YardSaleRequestDto yardSaleRequestDto) {
        YardSaleResponseDto updatedYardSale = yardSaleService.updateYardSale(yardSaleId, yardSaleRequestDto);
        return ResponseEntity.accepted().body(updatedYardSale);
    }

    @DeleteMapping("/{yardSaleId}")
    public ResponseEntity<Void> deleteYardSale(@RequestParam UUID userId, @PathVariable UUID yardSaleId) {
        yardSaleService.deleteYardSale(userId, yardSaleId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{yardSaleId}/items/{itemId}")
    public ResponseEntity<YardSaleResponseDto> addItemToYardSale(@RequestParam UUID userId, @PathVariable UUID yardSaleId, @PathVariable UUID itemId) {
        YardSaleResponseDto updatedYardSale = yardSaleService.addItemToYardSale(userId, yardSaleId, itemId);
        return ResponseEntity.ok(updatedYardSale);
    }

    @DeleteMapping("/{yardSaleId}/items/{itemId}")
    public ResponseEntity<YardSaleResponseDto> removeItemFromYardSale(@RequestParam UUID userId, @PathVariable UUID yardSaleId, @PathVariable UUID itemId) {
        YardSaleResponseDto updatedYardSale = yardSaleService.removeItemFromYardSale(userId, yardSaleId, itemId);
        return ResponseEntity.ok(updatedYardSale);
    }
} 