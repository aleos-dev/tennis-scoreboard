package com.aleos.match.creation;

import com.aleos.match.scoring.ScoringStrategy;
import com.aleos.match.stage.Stage;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

public final class StrategyProvider {

    private static final Map<StrategyType, Supplier<? extends ScoringStrategy<?>>> strategyMap = new EnumMap<>(StrategyType.class);

    static {
        for (StrategyType strategyType : StrategyType.values()) {
            strategyMap.put(strategyType, strategyType.getStrategySupplier());
        }
    }

    private StrategyProvider() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @SuppressWarnings("unchecked")
    public static <T extends Stage<?>, S extends ScoringStrategy<T>> S get(StrategyType strategyType) {
        Supplier<? extends ScoringStrategy<?>> supplier = strategyMap.get(strategyType);
        if (supplier == null) {
            throw new IllegalArgumentException("No strategy found for type: " + (strategyType != null ? strategyType.name() : "null"));
        }
        return (S) supplier.get();
    }
}
