package com.aleos.match.stage;

import com.aleos.match.model.enums.Player;
import com.aleos.match.model.enums.StageState;
import com.aleos.match.scoring.ScoringStrategy;
import com.aleos.match.score.ScoreManager;
import lombok.Getter;
import lombok.Setter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class AbstractStage<T extends Stage<M>, M extends ScoreManager<?>>
        implements Game<M>, TennisSet<M>, Match<M>, PropertyChangeListener {

    protected final ScoringStrategy<T> scoringStrategy;

    protected final PropertyChangeSupport support;

    @Getter
    @Setter
    protected StageState state = StageState.ONGOING;

    @Getter
    @Setter
    protected Player winner;

    protected AbstractStage(ScoringStrategy<T> strategy) {
        this.scoringStrategy = strategy;
        this.support = new PropertyChangeSupport(this);
    }

    protected abstract void handleScorePoint(Player pointWinner);

    @Override
    public void scorePoint(Player pointWinner) {
        if (isOver()) {
            throw new IllegalCallerException("%s is over. Start a new one.".formatted(this.getClass().getSimpleName()));
        }
        handleScorePoint(pointWinner);
    }

    @Override
    public boolean isOver() {
        return winner != null;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        support.firePropertyChange(propertyName, oldValue, newValue);
    }
}
