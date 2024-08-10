package com.aleos.match.stage;

import com.aleos.match.creation.Factory;
import com.aleos.match.model.enums.MatchEvent;
import com.aleos.match.model.enums.Player;
import com.aleos.match.scoremanager.ScoreManager;
import com.aleos.match.scoring.ScoringStrategy;
import lombok.Getter;

import java.beans.PropertyChangeEvent;
import java.lang.ref.WeakReference;
import java.util.Optional;
import java.util.function.Supplier;

import static com.aleos.match.model.enums.StageState.TIE_BREAK;

public class StandardSet<G extends TennisGame> extends AbstractStage<TennisSet> implements TennisSet {

    private final Factory<G> gameFactory;

    @Getter
    private G currentGame;

    public StandardSet(Supplier<ScoringStrategy<TennisSet>> strategySupplier,
                       Supplier<ScoreManager> managerSupplier,
                       Factory<G> gameFactory) {

        super(strategySupplier, managerSupplier);
        this.gameFactory = gameFactory;
    }

    @Override
    public void processScorePoint(Player pointWinner) {
        if (currentGame == null || currentGame.isOver()) {
            currentGame = gameFactory.create();
            currentGame.addPropertyChangeListener(new WeakReference<>(this).get());
            if (state == TIE_BREAK) {
                currentGame.setState(TIE_BREAK);
            }
        }
        currentGame.scorePoint(pointWinner);
    }

    @Override
    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    @Override
    protected void handleStageSpecificPropertyChange(PropertyChangeEvent event) {
        MatchEvent.fromEventName(event.getPropertyName())
                .filter(name -> name == MatchEvent.GAME_WINNER)
                .map(name -> event)
                .ifPresent(this::handleGameStageWinner);
    }

    private void handleGameStageWinner(PropertyChangeEvent event) {
        scoringStrategy.scorePoint(this, (Player) event.getNewValue());

        firePropertyChange(MatchEvent.SET_SCORES.getEventName(), null, scoreManager);
        if (isOver()) {
            firePropertyChange(MatchEvent.SET_WINNER.getEventName(), null, winner);
        }
    }

    @Override
    public Optional<Stage> getChildStage() {
        return Optional.ofNullable(currentGame);
    }
}
