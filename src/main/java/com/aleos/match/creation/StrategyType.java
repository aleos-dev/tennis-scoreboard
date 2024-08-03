package com.aleos.match.creation;

import com.aleos.match.scoring.ScoringStrategy;
import com.aleos.match.scoring.strategy.*;

import java.util.function.Supplier;

public enum StrategyType {
    STANDARD_GAME(StandardGameScoringStrategy::new),
    DESKTOP_GAME(DesktopGameScoringStrategy::new),
    FANCY_GAME(FancyGameScoringStrategy::new),
    STANDARD_SET(StandardSetScoringStrategy::new),
    BO3_MATCH(StandardMatchScoringStrategy.Bo3::new),
    BO5_MATCH(StandardMatchScoringStrategy.Bo5::new);

    private final Supplier<? extends ScoringStrategy<?>> strategySupplier;

    StrategyType(Supplier<? extends ScoringStrategy<?>> strategySupplier) {
        this.strategySupplier = strategySupplier;
    }

    public Supplier<? extends ScoringStrategy<?>> getStrategySupplier() {
        return strategySupplier;
    }
}
