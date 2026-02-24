package com.koerber.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchResponseDto {

    private Long batchId;
    private Integer quantity;
    private LocalDate expiryDate;
}
