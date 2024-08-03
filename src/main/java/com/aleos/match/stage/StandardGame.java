package com.aleos.match.stage;

import com.aleos.match.model.enums.Player;
import com.aleos.match.scoring.ScoringStrategy;
import com.aleos.match.score.ScoreManager;

import java.beans.PropertyChangeEvent;

public class StandardGame<M extends ScoreManager<?>> extends AbstractStage<Game<M>, M> {

    private final M scoreManager;

    public StandardGame(ScoringStrategy<Game<M>> strategy, M scoreManager) {
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
    public M getScoreManager() {
        return scoreManager;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // This method would react to changes if this class were observing other properties
    }
}
