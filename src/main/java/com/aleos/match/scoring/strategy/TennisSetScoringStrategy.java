package com.aleos.match.scoring.strategy;

import com.aleos.match.model.enums.Player;
import com.aleos.match.model.enums.StageState;
import com.aleos.match.scoremanager.ScoreManager;
import com.aleos.match.model.ScoreRecord;
import com.aleos.match.scoring.ScoringStrategy;
import com.aleos.match.stage.Stage;
import com.aleos.match.stage.TennisSet;

public class TennisSetScoringStrategy implements ScoringStrategy<TennisSet> {

    private static final int MIN_WON_GAME_COUNT_TO_WIN = 6;
    private static final int MIN_ADVANTAGE_TO_WIN = 2;

    private static final int TIE_BREAK_GAME_COUNT = 6;
    private static final int MIN_WON_GAME_COUNT_TO_WIN_TIE_BREAK = 1;


    @Override
    public void scorePoint(TennisSet set, Player player) {
        var initSetState = set.getState();
        if (isStageFinished(initSetState)) {
            return;
        }

        var manager = set.getScoreManager();
        var playerScore = manager.getScore(player) + 1;
        var opponentScore = manager.getScore(player.getOpponent());


        if (isWinningConditionMet(initSetState, playerScore, opponentScore)) {
            concludeStage(set, player);

        } else evaluateTieBreakCondition(set, playerScore, opponentScore);

        updateSetScore(set, initSetState, player);
    }

    private void evaluateTieBreakCondition(TennisSet set, int playerScore, int opponentScore) {
        if (shouldTriggerTieBreak(set.getState(), playerScore, opponentScore)) {
            set.setState(StageState.TIE_BREAK);
        }
    }

    private boolean shouldTriggerTieBreak(StageState setState, int playerScore, int opponentScore) {
        return !isTieBreak(setState)
               && playerScore == TIE_BREAK_GAME_COUNT
               && opponentScore == TIE_BREAK_GAME_COUNT;
    }

    private boolean isWinningConditionMet(StageState setState, int playerScore, int opponentScore) {
        return isTieBreak(setState)
                ? hasEnoughScoreToWin(playerScore, MIN_WON_GAME_COUNT_TO_WIN_TIE_BREAK)

                : hasEnoughScoreToWin(playerScore, MIN_WON_GAME_COUNT_TO_WIN)
                  && hasMinAdvantageToWin(playerScore, opponentScore, MIN_ADVANTAGE_TO_WIN);
    }

    private void updateSetScore(TennisSet set, StageState initSetState, Player player) {
        var currentManager = set.getScoreManager();
        var currentState = set.getState();

        if (checkForTieBreakWin(initSetState, currentState)) {

            Stage tieBreakGame = set.getChildStage()
                    .orElseThrow(() -> new IllegalStateException("The child stage should be present."));

            updateScoresFromTieBreakResults(currentManager, tieBreakGame.getScoreManager());

        } else {
            currentManager.awardScore(set.getState(), player);
        }
    }

    private boolean checkForTieBreakWin(StageState initState, StageState currentState) {
        return isStageFinished(currentState) && isTieBreak(initState);
    }

    private void updateScoresFromTieBreakResults(ScoreManager setScoreManager, ScoreManager gameScoreManager) {
        setScoreManager.setScore(ScoreRecord.of(
                StageState.FINISHED,
                gameScoreManager.getScore(Player.ONE),
                gameScoreManager.getScore(Player.TWO)
        ));
    }
}
