package com.koerber.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto implements Serializable {

    private Long orderId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private String status;
    private List<Long> reservedFromBatchIds;
    private String message;
}
