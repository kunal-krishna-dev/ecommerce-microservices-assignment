package com.koerber.inventory.strategy;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class InventoryStrategyFactory {

    private final Map<String, InventoryStrategy> strategyMap;

    public InventoryStrategyFactory(Map<String, InventoryStrategy> strategyMap) {
        this.strategyMap = strategyMap;
    }

    public InventoryStrategy getStrategy(String type) {

        InventoryStrategy strategy = strategyMap.get(type);

        if (strategy == null) {
            throw new IllegalArgumentException("Invalid inventory strategy type");
        }
        return strategy;
    }
}
