package com.aleos.match;

import com.aleos.match.model.ScoreRecord;
import com.aleos.match.model.enums.Player;
import com.aleos.match.stage.Stage;

import java.util.List;
import java.util.Random;

public final class TestUtil {

    private static final Random RANDOM = new Random();

    private TestUtil() {
        throw new UnsupportedOperationException("Util class, can not be instantiated");
    }

    public static Player randomPlayer() {
        return Player.values()[RANDOM.nextInt(Player.values().length)];
    }

    public static int getScore(Stage stage, Player player) {
        return stage.getScoreManager().getScore(player);
    }

    public static void setScore(Stage stage, int playerOneScore, int playerTwoScore) {
        stage.getScoreManager().setScore(ScoreRecord.of(stage.getState(), playerOneScore, playerTwoScore));
    }

    public static void simulateStage(Stage stage, List<Player> setWinners) {
        for (Player player : setWinners) {
            while (!stage.isOver()) {
                stage.scorePoint(player);
            }
        }
    }

    public static void simulateScore(Stage stage, List<Player> setWinners) {
        for (Player player : setWinners) {
            stage.scorePoint(player);
        }
    }

    public static void simulateScore(Stage stage, Player player, int count) {
        for (int i = 0; i < count; i++) {
            stage.scorePoint(player);
        }
    }
}
