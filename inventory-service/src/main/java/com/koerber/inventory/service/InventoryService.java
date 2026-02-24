package com.koerber.inventory.service;

import com.koerber.inventory.dto.InventoryRequestDto;
import com.koerber.inventory.dto.InventoryResponseDto;
import com.koerber.inventory.dto.ProductResponseDto;

public interface InventoryService {
    ProductResponseDto getInventory(Long productId, String strategyType);

    InventoryResponseDto updateInventory(InventoryRequestDto requestDto, String strategyType);
}
