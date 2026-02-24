package com.koerber.inventory.mapper;

import com.koerber.inventory.dto.BatchResponseDto;
import com.koerber.inventory.dto.ProductResponseDto;
import com.koerber.inventory.entity.Batch;
import com.koerber.inventory.entity.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {

    private ProductMapper() {
    }

    public static ProductResponseDto toDto(Product product, List<Batch> batches) {

        List<BatchResponseDto> batchDtos = batches
                .stream()
                .map(ProductMapper::toBatchDto)
                .collect(Collectors.toList());

        return ProductResponseDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .batches(batchDtos)
                .build();
    }

    private static BatchResponseDto toBatchDto(Batch batch) {

        return BatchResponseDto.builder()
                .batchId(batch.getBatchId())
                .quantity(batch.getQuantity())
                .expiryDate(batch.getExpiryDate())
                .build();
    }
}
