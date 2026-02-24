package com.koerber.order.service;

import com.koerber.order.dto.InventoryResponseDto;
import com.koerber.order.dto.OrderRequestDto;
import com.koerber.order.dto.OrderResponseDto;
import com.koerber.order.entity.Order;
import com.koerber.order.repository.OrderRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final RestTemplate restTemplate;
    private final OrderRepository orderRepository;

    public OrderServiceImpl(RestTemplate restTemplate, OrderRepository repository) {
        this.restTemplate = restTemplate;
        this.orderRepository = repository;
    }

    @Override
    public OrderResponseDto placeOrder(OrderRequestDto request) {

        String reserveInventoryUrl = "http://localhost:8081/inventory/update";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OrderRequestDto> entity = new HttpEntity<>(request, headers);

        ResponseEntity<InventoryResponseDto> response = restTemplate.exchange(
                reserveInventoryUrl,
                HttpMethod.POST,
                entity,
                InventoryResponseDto.class
        );

        if (!response.getStatusCode().is2xxSuccessful()
                || response.getBody() == null) {
            throw new RuntimeException("Inventory reservation failed");
        }

        InventoryResponseDto responseBody = response.getBody();

        String batchIdsAsString = responseBody.getBatchIds().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        Order order = new Order();
        order.setProductId(request.getProductId());
        order.setProductName(responseBody.getProductName());
        order.setQuantity(request.getQuantity());
        order.setStatus("PLACED");
        order.setReservedBatchIds(batchIdsAsString);
        order.setCreatedAt(LocalDate.now());
        order.setMessage("Order placed. Inventory reserved.");

        orderRepository.save(order);

        return OrderResponseDto.builder()
                .orderId(order.getOrderId())
                .productId(order.getProductId())
                .productName(order.getProductName())
                .quantity(order.getQuantity())
                .status(order.getStatus())
                .reservedFromBatchIds(responseBody.getBatchIds())
                .message(order.getMessage())
                .build();
    }
}
