package com.aleos.match.util;


import com.aleos.match.StandardGame;
import com.aleos.match.StandardMatch;
import com.aleos.match.StandardSet;
import com.aleos.match.interfaces.stage.Game;
import com.aleos.match.interfaces.stage.TennisSet;
import com.aleos.match.score.FancyScoreManager;
import com.aleos.match.score.NumericScoreManager;
import com.aleos.match.score.PointScoreManager;
import com.aleos.match.strategy.*;

public final class StageFactory {

    private StageFactory() {
        throw new UnsupportedOperationException("Util class can not be instantiated");
    }

    public static StandardGame<PointScoreManager> createStandardGame() {
        return new StandardGame<>(StrategySupplier.get(StandardGameScoringStrategy.class), new PointScoreManager());
    }

    public static StandardGame<NumericScoreManager> createStandardDesktopGame() {
        return new StandardGame<>(StrategySupplier.get(DesktopGameScoringStrategy.class), new NumericScoreManager());
    }

    public static StandardGame<FancyScoreManager> createFancyDesktopGame() {
        return new StandardGame<>(StrategySupplier.get(FancyGameScoringStrategy.class), new FancyScoreManager());
    }

    public static StandardSet<NumericScoreManager, Game<PointScoreManager>> createStandardSet() {
        return new StandardSet<>(StrategySupplier.get(StandardSetScoringStrategy.class), new NumericScoreManager(), StageFactory::createStandardGame);
    }

    public static StandardMatch<NumericScoreManager, TennisSet<NumericScoreManager>> createBo3Match() {
        return new StandardMatch<>(StrategySupplier.get(StandardMatchScoringStrategy.Bo3.class), new NumericScoreManager(), StageFactory::createStandardSet);
    }

    public static StandardMatch<NumericScoreManager, TennisSet<NumericScoreManager>> createBo5Match() {
        return new StandardMatch<>(StrategySupplier.get(StandardMatchScoringStrategy.Bo5.class), new NumericScoreManager(), StageFactory::createStandardSet);
    }
}
