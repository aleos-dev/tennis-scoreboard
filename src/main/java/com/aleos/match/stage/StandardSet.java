package com.aleos.match.stage;

import com.aleos.match.model.enums.Player;
import com.aleos.match.creation.Factory;
import com.aleos.match.scoring.ScoringStrategy;
import com.aleos.match.score.ScoreManager;

import java.beans.PropertyChangeEvent;

public class StandardSet<M extends ScoreManager<?>, C extends Stage<? extends ScoreManager<?>>>
        extends AbstractStage<TennisSet<M>, M> {

    private final Factory<C> gameFactory;

    private final M scoreManager;

    private C currentGame;

    public StandardSet(ScoringStrategy<TennisSet<M>> scoringStrategy,
                       M scoreManager,
                       Factory<C> gameFactory) {
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
    public M getScoreManager() {
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
