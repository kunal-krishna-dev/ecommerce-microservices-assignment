package com.koerber.inventory.strategy;

import com.koerber.inventory.entity.Batch;

import java.util.List;

public interface InventoryStrategy {
    List<Batch> getSortedBatches(List<Batch> batches);
}
