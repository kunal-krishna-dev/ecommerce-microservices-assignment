package com.koerber.order.controller;

import com.koerber.order.dto.InventoryResponseDto;
import com.koerber.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void placeOrder_shouldPersistOrder() throws Exception {

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

        String requestJson = """
                {
                    "productId": 1001,
                    "quantity": 2
                }
                """;

        mockMvc.perform(post("/order")
                        .contentType("application/json")
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId").value(1001))
                .andExpect(jsonPath("$.productName").value("Laptop"))
                .andExpect(jsonPath("$.reservedFromBatchIds").isArray());

        // Verify DB persistence
        assertEquals(1, orderRepository.findAll().size());
    }
}
