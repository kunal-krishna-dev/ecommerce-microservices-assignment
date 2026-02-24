package com.koerber.order.service;

import com.koerber.order.dto.InventoryResponseDto;
import com.koerber.order.dto.OrderRequestDto;
import com.koerber.order.dto.OrderResponseDto;
import com.koerber.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    OrderRepository orderRepository;
    @Mock
    RestTemplate restTemplate;
    @InjectMocks
    OrderServiceImpl service;

    @Test
    void placeOrder_success() {
        OrderRequestDto request = new OrderRequestDto();
        request.setProductId(1001L);
        request.setQuantity(2);

        InventoryResponseDto inventoryResponse = new InventoryResponseDto();
        inventoryResponse.setProductId(1001L);
        inventoryResponse.setProductName("Laptop");
        inventoryResponse.setBatchIds(List.of(1L, 2L));

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(InventoryResponseDto.class)
        )).thenReturn(ResponseEntity.ok(inventoryResponse));

        when(orderRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        OrderResponseDto response = service.placeOrder(request);

        // Assert
        assertEquals(1001L, response.getProductId());
        assertEquals("Laptop", response.getProductName());
        assertEquals(2, response.getReservedFromBatchIds().size());

        verify(restTemplate).exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(InventoryResponseDto.class)
        );

        verify(orderRepository).save(any());
    }
}
