package com.koerber.inventory.strategy;

import com.koerber.inventory.entity.Batch;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component("FEFO")
public class FefoInventoryStrategy implements InventoryStrategy {

    @Override
    public List<Batch> getSortedBatches(List<Batch> batches) {
        return batches.stream()
                .sorted(Comparator.comparing(Batch::getExpiryDate))
                .toList();
    }
}
