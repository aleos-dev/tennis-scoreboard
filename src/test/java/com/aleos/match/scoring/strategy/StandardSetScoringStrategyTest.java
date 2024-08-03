package com.aleos.match.scoring.strategy;

import com.aleos.match.TestUtil;
import com.aleos.match.creation.StageFactory;
import com.aleos.match.model.enums.Player;
import com.aleos.match.model.enums.StageState;
import com.aleos.match.score.manager.NumericScoreManager;
import com.aleos.match.stage.StandardSet;
import com.aleos.match.stage.TennisSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static com.aleos.match.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;

class StandardSetScoringStrategyTest {

    private TennisSet<NumericScoreManager> set;

    private Player player;

    private Player opponent;

    @BeforeEach
    void setUp() {
        StandardSetScoringStrategy strategy = new StandardSetScoringStrategy();
        set = new StandardSet<>(strategy, new NumericScoreManager(), StageFactory::createStandardGame);
        player = TestUtil.randomPlayer();
        opponent = player.getOpponent();
    }

    @Test
    void scorePoint_shouldStartValueBeZero_whenNewSetIsTested() {
        assertEquals(0, getScore(set, player), "Initial score for player should be zero.");
        assertEquals(0, getScore(set, opponent), "Initial score for opponent should be zero.");
    }

    @Test
    void scorePoint_shouldIncreaseWinSetCount_whenPlayerWinsSet() {
        // Simulate the player winning a game
        simulateGame(set, List.of(player));

        assertEquals(1, getScore(set, player), "Player should have won one set.");
        assertEquals(0, getScore(set, opponent), "Opponent should have won no set.");
    }

    @Test
    void scorePoint_shouldSetWinner_whenPlayerWinsSet() {
        // Simulate player winning 6 sets
        simulateSet(set, Collections.nCopies(6, player));

        assertEquals(player, set.getWinner(), "Player should be the winner of the set.");
    }

    @Test
    void scorePoint_shouldEnterTieBreak_whenGamesAreTiedAtSix() {
        // Simulate both players winning 6 games
        simulateGame(set, Collections.nCopies(5, player));
        simulateGame(set, Collections.nCopies(6, opponent));
        simulateGame(set, List.of(player));

        assertEquals(StageState.TIE_BREAK, set.getState(), "Set should enter tie-break state.");

        assertEquals(0, getScore(set, player), "Initial tie-break score for player should be zero.");
        assertEquals(0, getScore(set, player), "Initial tie-break score for opponent should be zero.");
    }

    @Test
    void scorePoint_shouldSetWinnerInTieBreak_whenPlayerWinsByTwo() {
        set.setState(StageState.TIE_BREAK);

        simulateGame(set, List.of(player, opponent, player, opponent, player, player));

        assertEquals(player, set.getWinner(), "Player should win in a tie-break.");
        assertEquals(4, getScore(set, player), "Player should have 4 score.");
        assertEquals(2, getScore(set, opponent), "Opponent should have 2 score.");
        assertEquals(StageState.FINISHED, set.getState(), "State for the set should be finished.");
    }

    @Test
    void scorePoint_shouldMaintainAdvantage_whenGamesAreNotEnoughToWin() {
        // Simulate a score such that player has 5 games and opponent has 4
        simulateGame(set, Collections.nCopies(5, player));
        simulateGame(set, Collections.nCopies(5, opponent));

        // Player wins one more game, should still not be the winner
        simulateGame(set, List.of(player));
        assertNull(set.getWinner(), "Player should not be the winner yet.");

        // Player wins one more game, now should be the winner
        simulateGame(set, List.of(player));
        assertNotNull(set.getWinner(), "The winner should be declared.");
    }
}
