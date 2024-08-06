package com.aleos.match.creation;

import com.aleos.match.scoring.strategy.PingPongGameScoringStrategy;
import com.aleos.match.scoring.strategy.TennisGameScoringStrategy;
import com.aleos.match.scoring.strategy.TennisMatchScoringStrategy;
import com.aleos.match.scoring.strategy.TennisSetScoringStrategy;

public final class StrategyProvider {

    private StrategyProvider() {
        throw new UnsupportedOperationException("Util class can not be instantiated");
    }

    public static TennisGameScoringStrategy getTennisGameStrategy() {
        return new TennisGameScoringStrategy();
    }

    public static PingPongGameScoringStrategy getPingPongGameStrategy() {
        return new PingPongGameScoringStrategy();
    }

    public static TennisSetScoringStrategy getTennisSetStrategy() {
        return new TennisSetScoringStrategy();
    }

    public static TennisMatchScoringStrategy.Bo3 getBo3TennisMatchStrategy() {
        return new TennisMatchScoringStrategy.Bo3();
    }

    public static TennisMatchScoringStrategy.Bo5 getBo5TennisMatchStrategy() {
        return new TennisMatchScoringStrategy.Bo5();
    }
}
