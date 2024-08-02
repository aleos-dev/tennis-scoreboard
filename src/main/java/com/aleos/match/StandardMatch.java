package com.aleos.match;

import com.aleos.match.enums.MatchFormat;
import com.aleos.match.enums.Player;
import com.aleos.match.interfaces.Factory;
import com.aleos.match.interfaces.ScoringStrategy;
import com.aleos.match.interfaces.stage.Match;
import com.aleos.match.interfaces.stage.Stage;
import com.aleos.match.score.ScoreManager;
import com.aleos.match.strategy.StandardMatchScoringStrategy;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.beans.PropertyChangeEvent;
import java.util.UUID;

@Getter
@Setter
public class StandardMatch<S extends ScoreManager<?>, T extends Stage<?>> extends AbstractStage<Match<S>, S> {

    private final Factory<T> setFactory;

    private final S scoreManager;

    private final UUID id = UUID.randomUUID();

    private final MatchFormat matchFormat;

    private T currentSet;

    public StandardMatch(@NonNull ScoringStrategy<Match<S>> strategy,
                         @NonNull S scoreManager,
                         @NonNull Factory<T> setFactory) {
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
    public S getScoreManager() {
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
