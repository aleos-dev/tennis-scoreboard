package com.aleos.match.scoring.strategy;

import com.aleos.match.model.enums.Player;
import com.aleos.match.model.enums.StageState;
import com.aleos.match.model.ScoreRecord;
import com.aleos.match.scoring.ScoringStrategy;
import com.aleos.match.stage.Stage;
import com.aleos.match.stage.TennisGame;

import static com.aleos.match.model.enums.StageState.*;

public class TennisGameScoringStrategy implements ScoringStrategy<TennisGame> {

    private static final int DEUCE_SCORE_VALUE = 3;

    private static final int MIN_ADVANTAGE_TO_WIN = 2;
    private static final int MIN_WON_GAME_COUNT_TO_WIN = 4;
    private static final int MIN_WON_GAME_COUNT_TO_WIN_TIE_BREAK = 7;

    @Override
    public void scorePoint(TennisGame game, Player player) {
        if (isStageFinished(game.getState())) {
            return;
        }

        var scoreManager = game.getScoreManager();
        var playerScore = scoreManager.getScore(player) + 1;
        var opponentScore = scoreManager.getScore(player.getOpponent());


        if (isWinningConditionMet(game.getState(), playerScore, opponentScore)) {
            concludeStage(game, player);

        } else evaluateDeuceCondition(game, playerScore, opponentScore);

        updateGameScore(game, player);
    }

    private void updateGameScore(Stage game, Player player) {
        if (game.getState() == DEUCE) {
            game.getScoreManager().setScore(ScoreRecord.of(StageState.DEUCE, DEUCE_SCORE_VALUE, DEUCE_SCORE_VALUE));

        } else {
            game.getScoreManager().awardScore(game.getState(), player);
        }
    }

    private boolean isWinningConditionMet(StageState state, int playerScore, int opponentScore) {
        int minScoreToWin = isTieBreak(state) ? MIN_WON_GAME_COUNT_TO_WIN_TIE_BREAK : MIN_WON_GAME_COUNT_TO_WIN;

        return hasEnoughScoreToWin(playerScore, minScoreToWin)
               && hasMinAdvantageToWin(playerScore, opponentScore, MIN_ADVANTAGE_TO_WIN);
    }

    private void evaluateDeuceCondition(Stage game, int playerScore, int opponentScore) {
        var currentState = game.getState();

        if (currentState == StageState.DEUCE) {
            game.setState(ONGOING);
        } else if (shouldTriggerDeuce(currentState, playerScore, opponentScore)) {
            game.setState(DEUCE);
        }
    }

    private boolean shouldTriggerDeuce(StageState initState, int playerScore, int opponentScore) {
        // A deuce occurs if both players have the same score at or above DEUCE_SCORE
        return initState != TIE_BREAK && playerScore >= DEUCE_SCORE_VALUE && opponentScore == playerScore;
    }
}
