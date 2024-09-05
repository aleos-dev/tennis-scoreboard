package com.aleos.service;

import com.aleos.match.model.ScoreRecord;
import com.aleos.match.model.enums.*;
import com.aleos.match.scoremanager.ScoreManager;
import com.aleos.match.stage.Stage;
import com.aleos.match.stage.StandardMatch;
import com.aleos.match.stage.TennisMatch;
import com.aleos.match.stage.TennisSet;
import com.aleos.model.MatchScore;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

public class ScoreTrackerService implements PropertyChangeListener {

    private final Map<UUID, MatchScore> scores = new ConcurrentHashMap<>();

    public Optional<MatchScore> findById(UUID id) {
        return Optional.ofNullable(scores.get(id));
    }

    public void trackMatch(TennisMatch match) {
        match.addPropertyChangeListener(new WeakReference<>(this).get());
        MatchScore matchScore = new MatchScore(match.getId(), match.getPlayerOneName(), match.getPlayerTwoName());
        initMatchScore(matchScore, match, StarPresentation::translateScores);
        scores.put(match.getId(), matchScore);
    }

    private void initMatchScore(MatchScore matchScore,
                                TennisMatch match,
                                BiFunction<StageType, List<ScoreRecord>, String[]> converter
    ) {
        matchScore.setScoreSets(match.getScoreManager().getScoresPresentation(converter));

        match.getChildStage().ifPresent(set -> {

            matchScore.setScoreGames(set.getScoreManager().getScoresPresentation(converter));

            set.getChildStage().ifPresent(game -> matchScore.setScorePoints(
                    game.getScoreManager().getScoresPresentation(converter)));
        });
    }


    public void untrackMatch(UUID id) {
        scores.remove(id);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Optional<MatchEvent> eventOpt = MatchEvent.fromEventName(evt.getPropertyName());

        eventOpt.ifPresent(matchEvent -> {
            if (isScoreEvent(matchEvent)) {
                handleScoreEvent(evt, matchEvent);
            } else if (isWinnerEvent(matchEvent)) {
                handleWinnerEvent(evt, matchEvent);
            }
        });
    }

    private boolean isScoreEvent(MatchEvent event) {
        return event == MatchEvent.GAME_SCORES || event == MatchEvent.SET_SCORES || event == MatchEvent.MATCH_SCORES;
    }

    private boolean isWinnerEvent(MatchEvent event) {
        return event == MatchEvent.GAME_WINNER || event == MatchEvent.SET_WINNER || event == MatchEvent.MATCH_WINNER;
    }

    private void handleScoreEvent(PropertyChangeEvent evt, MatchEvent event) {
        String[] newRecord = getScoreManager(evt).getScoresPresentation(StarPresentation::translateScores);
        UUID matchId = getMatchId(evt);
        MatchScore matchScore = scores.get(matchId);

        switch (event) {
            case GAME_SCORES -> {
                matchScore.getNotifications().clear();
                matchScore.setScorePoints(newRecord);
            }
            case SET_SCORES -> matchScore.setScoreGames(newRecord);
            case MATCH_SCORES -> matchScore.setScoreSets(newRecord);
            default -> throw new IllegalStateException("Unexpected value: " + event);
        }
    }

    private void handleWinnerEvent(PropertyChangeEvent evt, MatchEvent matchEvent) {
        UUID matchId = getMatchId(evt);
        MatchScore matchScore = scores.get(matchId);

        addWinnerNotification(evt, matchEvent, matchScore);

        switch (matchEvent) {
            case SET_WINNER -> {
                addSetScoresHistory(evt, matchScore);
                refreshPointsAndGames(matchScore);
            }
            case MATCH_WINNER -> untrackMatch(matchId);
            default -> { /*do nothing*/ }
        }
    }

    @SuppressWarnings("unchecked")
    private void addSetScoresHistory(PropertyChangeEvent evt, MatchScore matchScore) {
        var source = (StandardMatch<TennisSet>) evt.getSource();
        Stage setStage = source.getChildStage().orElseThrow();
        ScoreManager setScoreManager = setStage.getScoreManager();

        StringBuffer scoreState = matchScore.getScoreSnapshot();

        if (!scoreState.isEmpty()) {
            scoreState.append(",");
        }

        scoreState.append(setScoreManager.getScore(Player.ONE))
                .append(":")
                .append(setScoreManager.getScore(Player.TWO));

        handleTieBreakScoreRecord(matchScore, scoreState);
    }

    private static void refreshPointsAndGames(MatchScore matchScore) {
        matchScore.setScorePoints(new String[] {"",""});
        matchScore.setScoreGames(new String[] {"",""});
    }

    private String resolvePlayerName(Player player, MatchScore matchScore) {
        return player == Player.ONE ? matchScore.getPlayerOneName() : matchScore.getPlayerTwoName();
    }

    private void addWinnerNotification(PropertyChangeEvent evt, MatchEvent matchEvent, MatchScore matchScore) {
        String wonStage = getWonStage(matchEvent);
        Player player = (Player) evt.getNewValue();
        String playerName = resolvePlayerName(player, matchScore);

        matchScore.addNotification("%s wins %s".formatted(playerName, wonStage));
    }

    private String getWonStage(MatchEvent event) {
        return switch (event) {
            case GAME_WINNER -> "the game!";
            case SET_WINNER -> "the set!";
            case MATCH_WINNER -> "the match!";
            default -> throw new IllegalStateException("Unexpected event: " + event);
        };
    }

    private ScoreManager getScoreManager(PropertyChangeEvent evt) {
        return (ScoreManager) evt.getNewValue();
    }

    private UUID getMatchId(PropertyChangeEvent evt) {
        return ((TennisMatch) evt.getSource()).getId();
    }

    private void handleTieBreakScoreRecord(MatchScore matchScore, StringBuffer scoreState) {
        if (isTieBreakRecordFormat(matchScore.getScoreGames())) {
            int playerOneLastScoreIdx = Math.max(scoreState.lastIndexOf(",") + 1, 0);
            scoreState.insert(playerOneLastScoreIdx, "6(");
            int separatorIdx = scoreState.lastIndexOf(":");
            scoreState.insert(separatorIdx, ")");
            int playerTwoLastScoreIdx = separatorIdx + 2;
            scoreState.insert(playerTwoLastScoreIdx, "6(");
            scoreState.insert(scoreState.length(), ")");
        }
    }

    private boolean isTieBreakRecordFormat(String[] gamesScore) {
        return gamesScore[0].split("\\D").length > 1;
    }
}
