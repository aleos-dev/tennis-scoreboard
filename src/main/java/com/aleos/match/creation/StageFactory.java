package com.aleos.match.creation;


import com.aleos.match.stage.StandardGame;
import com.aleos.match.stage.StandardMatch;
import com.aleos.match.stage.StandardSet;
import com.aleos.match.stage.Game;
import com.aleos.match.stage.TennisSet;
import com.aleos.match.score.manager.FancyScoreManager;
import com.aleos.match.score.manager.NumericScoreManager;
import com.aleos.match.score.manager.PointScoreManager;

public final class StageFactory {

    private StageFactory() {
        throw new UnsupportedOperationException("Util class can not be instantiated");
    }

    public static StandardGame<PointScoreManager> createStandardGame() {
        return new StandardGame<>(StrategyProvider.get(StrategyType.STANDARD_GAME), new PointScoreManager());
    }

    public static StandardGame<NumericScoreManager> createStandardDesktopGame() {
        return new StandardGame<>(StrategyProvider.get(StrategyType.DESKTOP_GAME), new NumericScoreManager());
    }

    public static StandardGame<FancyScoreManager> createFancyDesktopGame() {
        return new StandardGame<>(StrategyProvider.get(StrategyType.FANCY_GAME), new FancyScoreManager());
    }

    public static StandardSet<NumericScoreManager, Game<PointScoreManager>> createStandardSet() {
        return new StandardSet<>(
                StrategyProvider.get(StrategyType.STANDARD_SET),
                new NumericScoreManager(),
                StageFactory::createStandardGame);
    }

    public static StandardMatch<NumericScoreManager, TennisSet<NumericScoreManager>> createBo3Match() {
        return new StandardMatch<>(
                StrategyProvider.get(StrategyType.BO3_MATCH),
                new NumericScoreManager(),
                StageFactory::createStandardSet);
    }

    public static StandardMatch<NumericScoreManager, TennisSet<NumericScoreManager>> createBo5Match() {
        return new StandardMatch<>(
                StrategyProvider.get(StrategyType.BO5_MATCH),
                new NumericScoreManager(),
                StageFactory::createStandardSet);
    }
}
