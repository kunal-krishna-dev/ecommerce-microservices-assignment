package com.koerber.order.controller;

import com.koerber.order.dto.OrderRequestDto;
import com.koerber.order.dto.OrderResponseDto;
import com.koerber.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order")
    public ResponseEntity<OrderResponseDto> placeOrder(
            @RequestBody OrderRequestDto request) {
        OrderResponseDto responseDto = orderService.placeOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
