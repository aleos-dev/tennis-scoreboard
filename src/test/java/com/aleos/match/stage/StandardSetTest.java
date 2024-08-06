package com.aleos.match.stage;

import com.aleos.match.TestUtil;
import com.aleos.match.creation.StageFactory;
import com.aleos.match.creation.StrategyProvider;
import com.aleos.match.model.enums.Player;
import com.aleos.match.model.enums.StageState;
import com.aleos.match.model.enums.StageType;
import com.aleos.match.scoremanager.StageScoreManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.aleos.match.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;

class StandardSetTest {


    private TennisSet set;

    private Player player;

    private Player opponent;

    private final int scoresToWinGame = 4;

    @BeforeEach
    void setUp() {
        set = new StandardSet<>(
                StrategyProvider::getTennisSetStrategy,
                () -> new StageScoreManager(StageType.SET),
                StageFactory::createStandardGame
        );
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
        simulateScore(set, player, scoresToWinGame * 4);
        simulateScore(set, opponent, scoresToWinGame);

        assertEquals(4, getScore(set, player), "Player should have won 4 games.");
        assertEquals(1, getScore(set, opponent), "Opponent should have won 1 set.");
    }

    @Test
    void scorePoint_shouldSetWinner_whenPlayerWinsSet() {
        // Simulate player winning 6 sets
        simulateStage(set, List.of(player));

        assertEquals(player, set.getWinner(), "Player should be the winner of the set.");
    }

    @Test
    void scorePoint_shouldEnterTieBreak_whenGamesAreTiedAtSix() {
        // Simulate both players winning 6 games
        simulateScore(set, player, scoresToWinGame * 5);
        simulateScore(set, opponent, scoresToWinGame * 6);
        simulateScore(set, player, scoresToWinGame);

        assertEquals(StageState.TIE_BREAK, set.getState(), "Set should enter tie-break state.");
    }

    @Test
    void scorePoint_shouldSetWinnerInTieBreak_whenPlayerWinsByTwoAndHaveMin7() {
        set.setState(StageState.TIE_BREAK);
        player = Player.ONE;
        opponent = player.getOpponent();

        simulateScore(set, player, 6);
        assertSame(StageState.TIE_BREAK, set.getState(), "The set state should be Tie Break.");
        assertNull(set.getWinner(), "Winner can not be declared.");

        simulateScore(set, opponent, 7);
        assertSame(StageState.TIE_BREAK, set.getState(), "The set state should be Tie Break.");
        assertNull(set.getWinner(), "Winner can not be declared.");

        simulateScore(set, List.of(player, opponent, player, opponent, player, player, player));

        assertEquals(player, set.getWinner(), "Player should win in a tie-break.");
        assertEquals(11, getScore(set, player), "Player should have 4 score.");
        assertEquals(9, getScore(set, opponent), "Opponent should have 2 score.");
        assertEquals(StageState.FINISHED, set.getState(), "State for the set should be finished.");
    }

    @Test
    void scorePoint_shouldMaintainAdvantage_whenGamesAreNotEnoughToWin() {
        // Simulate a score such that player and opponent have 5 games
        simulateScore(set, player, scoresToWinGame * 5);
        simulateScore(set, opponent, scoresToWinGame * 5);

        // Player wins one more game, should still not be the winner
        simulateScore(set, player, scoresToWinGame);
        assertNull(set.getWinner(), "Player should not be the winner yet.");

        // Player wins one more game, now should be the winner
        simulateScore(set, player, scoresToWinGame);
        assertNotNull(set.getWinner(), "The winner should be declared.");
    }
}