package com.aleos.match.stage;

import com.aleos.match.exception.StageIsOverException;
import com.aleos.match.model.enums.Player;
import com.aleos.match.model.enums.StageState;
import com.aleos.match.scoremanager.ScoreManager;
import com.aleos.match.scoring.ScoringStrategy;
import lombok.Getter;
import lombok.Setter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.function.Supplier;

import static com.aleos.match.model.enums.StageState.NOT_STARTED;
import static com.aleos.match.model.enums.StageState.ONGOING;

public abstract class AbstractStage<T extends Stage>
        implements Stage, PropertyChangeListener {

    @Getter
    public final ScoreManager scoreManager;

    protected final ScoringStrategy<T> scoringStrategy;

    protected final PropertyChangeSupport support;

    @Getter
    @Setter
    protected StageState state = NOT_STARTED;

    @Getter
    @Setter
    protected Player winner;

    protected AbstractStage(Supplier<ScoringStrategy<T>> strategySupplier,
                            Supplier<ScoreManager> managerSupplier) {
        this.scoringStrategy = strategySupplier.get();
        this.scoreManager = managerSupplier.get();
        this.support = new PropertyChangeSupport(this);
    }

    protected abstract void processScorePoint(Player pointWinner);

    protected abstract void handleStageSpecificPropertyChange(PropertyChangeEvent event);

    @Override
    public void scorePoint(Player pointWinner) {
        if (isOver()) {
            throw new StageIsOverException("%s is over. Start a new one.".formatted(this.getClass().getSimpleName()));
        }

        if (state == NOT_STARTED) { state = ONGOING; }

        processScorePoint(pointWinner);
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

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        rethrowEventUp(event);
        handleStageSpecificPropertyChange(event);
    }

    private void rethrowEventUp(PropertyChangeEvent event) {
        firePropertyChange(event.getPropertyName(), event.getOldValue(), event.getNewValue());
    }
}
