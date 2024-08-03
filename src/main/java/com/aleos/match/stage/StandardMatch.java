package com.aleos.match.stage;

import com.aleos.match.creation.Factory;
import com.aleos.match.model.enums.MatchFormat;
import com.aleos.match.model.enums.Player;
import com.aleos.match.score.ScoreManager;
import com.aleos.match.scoring.ScoringStrategy;
import com.aleos.match.scoring.strategy.StandardMatchScoringStrategy;
import lombok.Getter;
import lombok.NonNull;

import java.beans.PropertyChangeEvent;
import java.util.UUID;

public class StandardMatch<M extends ScoreManager<?>, C extends Stage<? extends ScoreManager<?>>> extends AbstractStage<Match<M>, M> {

    private final Factory<C> setFactory;

    private final M scoreManager;

    @Getter
    private final UUID id = UUID.randomUUID();

    @Getter
    private final MatchFormat matchFormat;

    private C currentSet;

    public StandardMatch(@NonNull ScoringStrategy<Match<M>> strategy,
                         @NonNull M scoreManager,
                         @NonNull Factory<C> setFactory) {
        super(strategy);
        this.scoreManager = scoreManager;
        this.setFactory = setFactory;
        this.matchFormat = strategy.getClass().isAssignableFrom(StandardMatchScoringStrategy.Bo3.class)
                ? MatchFormat.BO3
                : MatchFormat.BO5;
    }

    @Override
    public void handleScorePoint(@NonNull Player pointWinner) {
        if (currentSet == null || currentSet.isOver()) {
            currentSet = setFactory.create();
            currentSet.addPropertyChangeListener(this);
        }
        currentSet.scorePoint(pointWinner);
    }

    @Override
    public M getScoreManager() {
        return scoreManager;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if ("setWinner".equals(event.getPropertyName())) {
            super.scoringStrategy.scorePoint(this, ((Player) event.getNewValue()));

            if (isOver()) {
                firePropertyChange("matchWinner", null, winner);
            } else {
                // check old value for reference (it is cloned?)
                firePropertyChange("matchScores", event.getOldValue(), scoreManager);
            }

        } else if ("setScores".equals(event.getPropertyName())) {
            firePropertyChange("setScores", event.getOldValue(), event.getNewValue());

        } else if ("gameScores".equals(event.getPropertyName())) {
            firePropertyChange("gameScores", event.getOldValue(), event.getNewValue());
        }
    }
}
