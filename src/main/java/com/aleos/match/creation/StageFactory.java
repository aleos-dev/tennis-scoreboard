package com.aleos.match.creation;


import com.aleos.match.model.enums.StageType;
import com.aleos.match.scoremanager.StageScoreManager;
import com.aleos.match.stage.*;

public final class StageFactory {

    private StageFactory() {
        throw new UnsupportedOperationException("Util class can not be instantiated");
    }

    public static StandardGame createStandardGame() {
        return new StandardGame(StrategyProvider::getTennisGameStrategy, () -> new StageScoreManager(StageType.GAME));
    }

    public static StandardGame createStandardDesktopGame() {
        return new StandardGame(StrategyProvider::getPingPongGameStrategy, () -> new StageScoreManager(StageType.GAME));
    }

    public static StandardSet<TennisGame> createStandardSet() {
        return new StandardSet<>(StrategyProvider::getTennisSetStrategy,
                () -> new StageScoreManager(StageType.SET),
                StageFactory::createStandardGame);
    }

    public static StandardMatch<TennisSet> createBo3Match() {
        return new StandardMatch<>(
                StrategyProvider::getBo3TennisMatchStrategy,
                () -> new StageScoreManager(StageType.MATCH),
                StageFactory::createStandardSet);
    }

    public static StandardMatch<TennisSet> createBo5Match() {
        return new StandardMatch<>(
                StrategyProvider::getBo5TennisMatchStrategy,
                () -> new StageScoreManager(StageType.MATCH),
                StageFactory::createStandardSet);
    }
}