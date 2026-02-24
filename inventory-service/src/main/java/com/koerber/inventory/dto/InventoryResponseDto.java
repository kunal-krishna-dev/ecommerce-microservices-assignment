package com.koerber.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponseDto {
    private Long productId;
    private String productName;
    private List<Long> batchIds = new ArrayList<>();
}
