package com.aleos.match.scoring.strategy;

import com.aleos.match.model.enums.Player;
import com.aleos.match.scoring.ScoringStrategy;
import com.aleos.match.stage.Game;
import com.aleos.match.score.manager.NumericScoreManager;

public class DesktopGameScoringStrategy implements ScoringStrategy<Game<NumericScoreManager>> {

    private static final int MIN_GAME_COUNT_TO_WIN = 11;

    private static final int MIN_ADVANTAGE_TO_WIN = 2;

    @Override
    public void scorePoint(Game<NumericScoreManager> game, Player player) {
        var manager = game.getScoreManager();
        manager.awardPoint(player);

        if (isWinningConditionMet(manager, player)) {
            game.setWinner(player);
        }
    }

    protected boolean isWinningConditionMet(NumericScoreManager manager, Player player) {
        int wonByPlayer = manager.getScore(player);
        int wonByOpponent = manager.getScore(player.getOpponent());

        return hasEnoughScoreToWin(wonByPlayer, MIN_GAME_COUNT_TO_WIN)
               && hasMinAdvantageToWin(wonByPlayer, wonByOpponent, MIN_ADVANTAGE_TO_WIN);
    }
}
