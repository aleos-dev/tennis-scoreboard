package com.aleos.match.scoring.strategy;

import com.aleos.match.model.enums.FancyPoint;
import com.aleos.match.model.enums.Player;
import com.aleos.match.scoring.ScoringStrategy;
import com.aleos.match.stage.Game;
import com.aleos.match.score.manager.FancyScoreManager;

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
