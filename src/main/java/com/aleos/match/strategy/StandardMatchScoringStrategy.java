package com.aleos.match.strategy;

import com.aleos.match.enums.Player;
import com.aleos.match.interfaces.ScoringStrategy;
import com.aleos.match.interfaces.stage.Match;
import com.aleos.match.score.NumericScoreManager;

public abstract class StandardMatchScoringStrategy implements ScoringStrategy<Match<NumericScoreManager>> {

    protected final int minGameCountToWin;

    protected StandardMatchScoringStrategy(int minGameCountToWin) {
        this.minGameCountToWin = minGameCountToWin;
    }

    @Override
    public void scorePoint(Match<NumericScoreManager> stage, Player player) {
        var manager = stage.getScoreManager();
        manager.awardPoint(player);

        int playerScore = manager.getScore(player);

        if (hasEnoughScoreToWin(playerScore, minGameCountToWin)) {
            stage.setWinner(player);
        }
    }

    public static class Bo3 extends StandardMatchScoringStrategy {
        public Bo3() {
            super(2);
        }
    }

    public static class Bo5 extends StandardMatchScoringStrategy {
        public Bo5() {
            super(3);
        }
    }
}
