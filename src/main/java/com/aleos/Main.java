package com.aleos;

import com.aleos.match.enums.Player;
import com.aleos.match.score.FancyScoreManager;
import com.aleos.match.score.PointScoreManager;
import com.aleos.match.util.StageFactory;

import java.beans.PropertyChangeEvent;

public class Main {
    public static void main(String[] args) {
        var bo3Match = StageFactory.createBo5Match();
        bo3Match.addPropertyChangeListener((PropertyChangeEvent evt) -> {
            // Logic to handle the property change
            if ("gameScores".equals(evt.getPropertyName())) {
                System.out.println("game win: " + ((PointScoreManager) evt.getNewValue()).getScore(Player.ONE));
            }

            if ("setScores".equals(evt.getPropertyName())) {
                System.out.println("set win: " + evt.getNewValue());
            }
        });
        var standardGame = StageFactory.createFancyDesktopGame();

        standardGame.addPropertyChangeListener((PropertyChangeEvent evt) -> {
            // Logic to handle the property change
            if ("gameScores".equals(evt.getPropertyName())) {
                System.out.println("game scores: " + ((FancyScoreManager) evt.getNewValue()).getScore(Player.ONE));
            }
        });


        System.out.println("Match created");
        for (int i = 0; i < 72; i++) {
            bo3Match.scorePoint(Player.ONE);
//            standardGame.scorePoint(Player.ONE);
        }

        System.out.println("MatchWInner: " + bo3Match.getWinner());
        System.out.println("GameWInner: " + standardGame.getWinner());
    }
}
