package com.aleos.match;

import com.aleos.match.enums.Player;
import com.aleos.match.interfaces.Factory;
import com.aleos.match.interfaces.ScoringStrategy;
import com.aleos.match.interfaces.stage.Stage;
import com.aleos.match.interfaces.stage.TennisSet;
import com.aleos.match.score.ScoreManager;

import java.beans.PropertyChangeEvent;

public class StandardSet<S extends ScoreManager<?>, G extends Stage<?>> extends AbstractStage<TennisSet<S>, S> {

    private final Factory<G> gameFactory;

    private final S scoreManager;

    private G currentGame;

    public StandardSet(ScoringStrategy<TennisSet<S>> scoringStrategy,
                       S scoreManager,
                       Factory<G> gameFactory) {
        super(scoringStrategy);
        this.scoreManager = scoreManager;
        this.gameFactory = gameFactory;
    }

    @Override
    public void handleScorePoint(Player pointWinner) {
        if (currentGame == null || currentGame.isOver()) {
            currentGame = gameFactory.create();
            currentGame.addPropertyChangeListener(this);
        }
        currentGame.scorePoint(pointWinner);
    }

    @Override
    public S getScoreManager() {
        return scoreManager;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {

        if ("gameWinner".equals(event.getPropertyName())) {
            super.scoringStrategy.scorePoint(this, (Player) event.getNewValue());

            if (isOver()) {
                firePropertyChange("setWinner", null, winner);
            } else {
                firePropertyChange("setScores", event.getOldValue(), scoreManager);
            }

        } else if ("gameScores".equals(event.getPropertyName())) {
            firePropertyChange("gameScores", event.getOldValue(), event.getNewValue());
        }
    }
}
