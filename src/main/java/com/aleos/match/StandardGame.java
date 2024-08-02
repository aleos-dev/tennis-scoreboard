package com.aleos.match;

import com.aleos.match.enums.Player;
import com.aleos.match.interfaces.ScoringStrategy;
import com.aleos.match.interfaces.stage.Game;
import com.aleos.match.score.ScoreManager;

import java.beans.PropertyChangeEvent;

public class StandardGame<S extends ScoreManager<?>> extends AbstractStage<Game<S>, S> {

    private final S scoreManager;

    public StandardGame(ScoringStrategy<Game<S>> strategy, S scoreManager) {
        super(strategy);
        this.scoreManager = scoreManager;
    }

    @Override
    public void handleScorePoint(Player pointWinner) {
        super.scoringStrategy.scorePoint(this, pointWinner);

        if (isOver()) {
            firePropertyChange("gameWinner", null, winner);
        } else {
            firePropertyChange("gameScores", null, scoreManager);
        }
    }

    @Override
    public S getScoreManager() {
        return scoreManager;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // This method would react to changes if this class were observing other properties
    }
}
