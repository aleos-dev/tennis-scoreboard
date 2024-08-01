package com.aleos.match;

import com.aleos.match.enums.Player;
import com.aleos.match.interfaces.ScoringStrategy;
import com.aleos.match.interfaces.stage.Game;
import com.aleos.match.score.PointScoreManager;

import java.beans.PropertyChangeEvent;
import java.util.Map;

public class StandardGame extends AbstractStage<PointScoreManager, Game<PointScoreManager>> {

    public StandardGame(ScoringStrategy<Game<PointScoreManager>> strategy) {
        super(strategy);
        initScores();
    }

    @Override
    public void scorePoint(Player pointWinner) {
        super.scoringStrategy.scorePoint(this, pointWinner);

        if (isOver()) {
            firePropertyChange("gameWinner", null, winner);
        } else {
            firePropertyChange("gameScores", null, scores);
        }
    }

    @Override
    public Map<Player, PointScoreManager> getScores() {
        return scores;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // This method would react to changes if this class were observing other properties
    }

    protected void initScores() {
        scores.put(Player.ONE, new PointScoreManager());
        scores.put(Player.TWO, new PointScoreManager());
    }
}
