package com.aleos.match.scoring.strategy;

import com.aleos.match.model.enums.MatchFormat;
import com.aleos.match.model.enums.Player;
import com.aleos.match.scoring.ScoringStrategy;
import com.aleos.match.stage.TennisMatch;

public abstract class TennisMatchScoringStrategy implements ScoringStrategy<TennisMatch> {

    protected final MatchFormat matchFormat;

    protected TennisMatchScoringStrategy(MatchFormat matchFormat) {
        this.matchFormat = matchFormat;
    }

    @Override
    public void scorePoint(TennisMatch match, Player player) {
        if (isStageFinished(match.getState())) {
            return;
        }

        var manager = match.getScoreManager();
        var playerScore = manager.getScore(player) + 1;

        if (hasEnoughScoreToWin(playerScore, matchFormat.getSetsRequiredToWin())) {
            concludeStage(match, player);
        }

        manager.awardScore(match.getState(), player);
    }

    public static class Bo3 extends TennisMatchScoringStrategy {
        public Bo3() {
            super(MatchFormat.BO3);
        }
    }

    public static class Bo5 extends TennisMatchScoringStrategy {
        public Bo5() {
            super(MatchFormat.BO5);
        }
    }
}
