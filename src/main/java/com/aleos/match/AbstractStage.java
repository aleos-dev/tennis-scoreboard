package com.aleos.match;

import com.aleos.match.enums.Player;
import com.aleos.match.enums.StageState;
import com.aleos.match.interfaces.ScoringStrategy;
import com.aleos.match.interfaces.stage.Game;
import com.aleos.match.interfaces.stage.Match;
import com.aleos.match.interfaces.stage.Stage;
import com.aleos.match.interfaces.stage.TennisSet;
import com.aleos.match.score.ScoreManager;
import lombok.Getter;
import lombok.Setter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class AbstractStage<T extends Stage<?>, S extends ScoreManager<?>>
        implements Game<S>, TennisSet<S>, Match<S>, PropertyChangeListener {

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
