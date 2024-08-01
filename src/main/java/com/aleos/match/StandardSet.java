package com.aleos.match;

import com.aleos.match.enums.Player;
import com.aleos.match.factory.Factory;
import com.aleos.match.interfaces.ScoringStrategy;
import com.aleos.match.interfaces.stage.Game;
import com.aleos.match.interfaces.stage.TennisSet;
import com.aleos.match.score.NumericScoreManager;
import com.aleos.match.score.PointScoreManager;

import java.beans.PropertyChangeEvent;
import java.util.Map;

public class StandardSet extends AbstractStage<NumericScoreManager, TennisSet<NumericScoreManager>> {

    private final Factory<StandardGame> gameFactory;

    private Game<PointScoreManager> currentGame;


    public StandardSet(ScoringStrategy<TennisSet<NumericScoreManager>> scoringStrategy,
                       Factory<StandardGame> gameFactory) {
        super(scoringStrategy);
        this.gameFactory = gameFactory;
    }

    @Override
    public void scorePoint(Player pointWinner) {
        if (currentGame == null || currentGame.isOver()) {
            currentGame = gameFactory.create();
            currentGame.addPropertyChangeListener(this);
        }
        currentGame.scorePoint(pointWinner);
    }

    @Override
    public Map<Player, NumericScoreManager> getScores() {
        return scores;
    }

    protected void initScores() {
        scores.put(Player.ONE, new NumericScoreManager());
        scores.put(Player.TWO, new NumericScoreManager());
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {

        if ("gameWinner".equals(event.getPropertyName())) {
            super.scoringStrategy.scorePoint(this, (Player) event.getNewValue());

            if (isOver()) {
                firePropertyChange("setWinner", null, winner);
            } else {
                firePropertyChange("setScores", event.getOldValue(), scores);
            }

        } else if ("gameScores".equals(event.getPropertyName())) {
            firePropertyChange("gameScores", event.getOldValue(), event.getNewValue());
        }
    }
}
