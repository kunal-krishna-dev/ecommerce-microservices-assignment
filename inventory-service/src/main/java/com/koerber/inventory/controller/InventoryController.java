package com.koerber.inventory.controller;

import com.koerber.inventory.dto.InventoryRequestDto;
import com.koerber.inventory.dto.InventoryResponseDto;
import com.koerber.inventory.dto.ProductResponseDto;
import com.koerber.inventory.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getInventory(
            @PathVariable Long productId,
            @RequestParam(required = false) String strategy) {

        ProductResponseDto productResponseDto = service.getInventory(productId, strategy);
        return ResponseEntity.ok(productResponseDto);
    }

    @PostMapping("/update")
    public ResponseEntity<InventoryResponseDto> updateInventory(
            @RequestBody InventoryRequestDto requestDto,
            @RequestParam(required = false) String strategy) {
        // updates inventory after an order is placed
        InventoryResponseDto responseDto = service.updateInventory(requestDto, strategy);
        return ResponseEntity.ok(responseDto);
    }
}
