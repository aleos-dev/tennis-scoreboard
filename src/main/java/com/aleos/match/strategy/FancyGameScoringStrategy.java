package com.aleos.match.strategy;

import com.aleos.match.enums.FancyPoint;
import com.aleos.match.enums.Player;
import com.aleos.match.interfaces.ScoringStrategy;
import com.aleos.match.interfaces.stage.Game;
import com.aleos.match.score.FancyScoreManager;

public class FancyGameScoringStrategy implements ScoringStrategy<Game<FancyScoreManager>> {

    private static final FancyPoint MIN_GAME_SCORE_VALUE_TO_WIN = FancyPoint.ELEVEN;

    @Override
    public void scorePoint(Game<FancyScoreManager> game, Player player) {
        var manager = game.getScoreManager();
        manager.awardPoint(player);

        FancyPoint playerScore = manager.getScore(player);

        if (isWinningConditionMet(playerScore)) {
            game.setWinner(player);
        }
    }

    private boolean isWinningConditionMet(FancyPoint playerScore) {
        return playerScore == MIN_GAME_SCORE_VALUE_TO_WIN;
    }
}
