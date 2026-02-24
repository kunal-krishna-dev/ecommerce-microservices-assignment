package com.koerber.inventory.service;

import com.koerber.inventory.dto.InventoryRequestDto;
import com.koerber.inventory.dto.InventoryResponseDto;
import com.koerber.inventory.dto.ProductResponseDto;
import com.koerber.inventory.entity.Batch;
import com.koerber.inventory.entity.Product;
import com.koerber.inventory.mapper.ProductMapper;
import com.koerber.inventory.repository.ProductRepository;
import com.koerber.inventory.strategy.InventoryStrategyFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final ProductRepository productRepository;
    private final InventoryStrategyFactory inventoryStrategyFactory;

    public InventoryServiceImpl(ProductRepository repository, InventoryStrategyFactory strategyFactory) {
        this.productRepository = repository;
        this.inventoryStrategyFactory = strategyFactory;
    }

    @Override
    public ProductResponseDto getInventory(Long productId, String strategyType) {

        if (strategyType == null || strategyType.isEmpty()) {
            strategyType = "FEFO";
        }

        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        List<Batch> sortedBatchesByExpiryDate = inventoryStrategyFactory.getStrategy(strategyType)
                .getSortedBatches(product.getBatches());

        return ProductMapper.toDto(product, sortedBatchesByExpiryDate);
    }

    @Override
    @Transactional
    public InventoryResponseDto updateInventory(InventoryRequestDto requestDto, String strategyType) {

        if (strategyType == null || strategyType.isEmpty()) {
            strategyType = "FEFO";
        }
        Product product = productRepository.findByProductId(requestDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + requestDto.getProductId()));

        InventoryResponseDto responseDto = new InventoryResponseDto();
        responseDto.setProductId(product.getProductId());
        responseDto.setProductName(product.getProductName());
        List<Long> batchIds = new ArrayList<>();

        List<Batch> batches = product.getBatches();

        List<Batch> sortedBatches = inventoryStrategyFactory.getStrategy(strategyType)
                .getSortedBatches(batches);

        int quantityToDeduct = requestDto.getQuantity();

        for (Batch batch : sortedBatches) {
            if (quantityToDeduct <= 0) {
                break;
            }

            int availableQuantity = batch.getQuantity();

            if (availableQuantity >= quantityToDeduct) {
                batch.setQuantity(availableQuantity - quantityToDeduct);
                quantityToDeduct = 0;
                batchIds.add(batch.getBatchId());
            } else {
                batch.setQuantity(0);
                quantityToDeduct -= availableQuantity;
                batchIds.add(batch.getBatchId());
            }
        }
        responseDto.setBatchIds(batchIds);

        if (quantityToDeduct > 0) {
            throw new RuntimeException("Not enough stock available to fulfill the order");
        }
        return responseDto;
    }
}
