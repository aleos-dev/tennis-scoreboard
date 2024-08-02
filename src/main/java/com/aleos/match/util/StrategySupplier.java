package com.aleos.match.util;

import com.aleos.match.interfaces.ScoringStrategy;
import com.aleos.match.interfaces.stage.Stage;
import com.aleos.match.strategy.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class StrategySupplier {

    private static final Map<Class<?>, Supplier<ScoringStrategy<?>>> strategyMap = new HashMap<>();

    static {
        strategyMap.put(StandardGameScoringStrategy.class, StandardGameScoringStrategy::new);
        strategyMap.put(DesktopGameScoringStrategy.class, DesktopGameScoringStrategy::new);
        strategyMap.put(FancyGameScoringStrategy.class, FancyGameScoringStrategy::new);
        strategyMap.put(StandardSetScoringStrategy.class, StandardSetScoringStrategy::new);
        strategyMap.put(StandardMatchScoringStrategy.Bo3.class, StandardMatchScoringStrategy.Bo3::new);
        strategyMap.put(StandardMatchScoringStrategy.Bo5.class, StandardMatchScoringStrategy.Bo5::new);
    }

    private StrategySupplier() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @SuppressWarnings("unchecked")
    public static <T extends Stage<?>> ScoringStrategy<T> get(Class<?> strategyClass) {
        Supplier<ScoringStrategy<?>> supplier = strategyMap.get(strategyClass);
        if (supplier != null) {
            return (ScoringStrategy<T>) supplier.get();
        }
        throw new IllegalArgumentException("No strategy found for type: " + strategyClass);
    }
}
