package com.koerber.inventory.service;

import com.koerber.inventory.dto.InventoryRequestDto;
import com.koerber.inventory.dto.InventoryResponseDto;
import com.koerber.inventory.entity.Batch;
import com.koerber.inventory.entity.Product;
import com.koerber.inventory.repository.ProductRepository;
import com.koerber.inventory.strategy.FefoInventoryStrategy;
import com.koerber.inventory.strategy.InventoryStrategyFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {
    @Mock
    ProductRepository productRepository;
    @Mock
    InventoryStrategyFactory strategyFactory;
    @InjectMocks
    InventoryServiceImpl service;

    @Test
    void reserveInventory_success() {
        // prepare product + batches
        Product p = new Product();
        p.setProductId(1001L);
        p.setProductName("Laptop");

        Batch b1 = new Batch();
        b1.setBatchId(1L);
        b1.setQuantity(2);
        b1.setExpiryDate(LocalDate.of(2025,12,31));
        b1.setProduct(p);

        Batch b2 = new Batch();
        b2.setBatchId(2L);
        b2.setQuantity(3);
        b2.setExpiryDate(LocalDate.of(2026,3,1));
        b2.setProduct(p);

        p.setBatches(List.of(b1,b2));

        when(productRepository.findByProductId(1001L)).thenReturn(Optional.of(p));
        when(strategyFactory.getStrategy("FEFO")).thenReturn(new FefoInventoryStrategy());

        InventoryRequestDto req = new InventoryRequestDto();
        req.setProductId(1001L);
        req.setQuantity(4);

        InventoryResponseDto resp = service.updateInventory(req, "FEFO");

        assertNotNull(resp);
        assertEquals(1001L, resp.getProductId());
        assertEquals("Laptop", resp.getProductName());
        assertEquals(2, resp.getBatchIds().size());

        // verify quantities updated correctly
        assertEquals(0, b1.getQuantity()); // fully consumed
        assertEquals(1, b2.getQuantity()); // 3 - 2 = 1 left

        verify(productRepository, times(1))
                .findByProductId(1001L);

        verify(strategyFactory, times(1))
                .getStrategy("FEFO");

        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void reserveInventory_insufficientStock_shouldThrowException() {

        Product product = new Product();
        product.setProductId(1001L);

        Batch batch = new Batch();
        batch.setBatchId(1L);
        batch.setQuantity(2);
        batch.setExpiryDate(LocalDate.of(2025, 12, 31));
        batch.setProduct(product);

        product.setBatches(List.of(batch));

        when(productRepository.findByProductId(1001L))
                .thenReturn(Optional.of(product));

        when(strategyFactory.getStrategy("FEFO"))
                .thenReturn(new FefoInventoryStrategy());

        InventoryRequestDto request = new InventoryRequestDto();
        request.setProductId(1001L);
        request.setQuantity(5);

        // Act + Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.updateInventory(request, "FEFO")
        );

        assertEquals("Not enough stock available to fulfill the order",
                exception.getMessage());

        verify(productRepository, times(1))
                .findByProductId(1001L);
    }
}
