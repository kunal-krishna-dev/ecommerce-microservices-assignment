package com.koerber.order.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private String status;
    private String reservedBatchIds;
    private LocalDate createdAt;
    private String message;
}
