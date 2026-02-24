package com.koerber.order.service;

import com.koerber.order.dto.OrderRequestDto;
import com.koerber.order.dto.OrderResponseDto;

public interface OrderService {

    OrderResponseDto placeOrder(OrderRequestDto request);
}
