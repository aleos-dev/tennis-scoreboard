package com.aleos.service;

import com.aleos.ImageService;
import com.aleos.exception.PlayerRegistrationException;
import com.aleos.exception.UnknownMatchFormat;
import com.aleos.match.creation.StageFactory;
import com.aleos.match.model.enums.MatchEvent;
import com.aleos.match.stage.TennisMatch;
import com.aleos.model.entity.Match;
import com.aleos.model.entity.Player;
import com.aleos.model.in.MatchPayload;
import com.aleos.model.in.PageablePayload;
import com.aleos.model.out.MatchDto;
import com.aleos.repository.MatchRepository;
import com.aleos.repository.PlayerDao;
import lombok.RequiredArgsConstructor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class MatchService implements PropertyChangeListener {

    private final MatchRepository matchRepository;

    private final PlayerDao playerDao;

    private final ScoreTrackerService scoreTrackerService;


//    private final Mapper mapper;

    public void createMatch(MatchPayload payload) {

        TennisMatch newMatch = createNewMatch(payload.format());
        registerPlayers(newMatch, payload);
        registerNewMatch(newMatch);
    }

    private void registerNewMatch(TennisMatch newMatch) {
        if (matchRepository.findOngoing(newMatch.getId()).isEmpty()) {

            newMatch.addPropertyChangeListener(new WeakReference<>(scoreTrackerService).get());
            matchRepository.cacheOngoing(newMatch);
            scoreTrackerService.trackMatch(newMatch);
        }
    }


    public List<MatchDto> findAll(PageablePayload pageable) {
        return null;
    }

    public Optional<TennisMatch> getAnyOngoing() {
        return matchRepository.findAllOngoing().stream().findAny();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (MatchEvent.MATCH_WINNER.getEventName().equals((evt.getPropertyName()))) {
            TennisMatch finishedMatch = (TennisMatch) evt.getSource();
            handleFinishedMatch(finishedMatch);
        }
    }

    private TennisMatch createNewMatch(String format) {
        return switch (format) {
            case "bo3" -> StageFactory.createBo3Match();
            case "bo5" -> StageFactory.createBo5Match();
            default -> throw new UnknownMatchFormat("Match format: %s is not supported.".formatted(format));
        };
    }

    private void handleFinishedMatch(TennisMatch finishedMatch) {
        matchRepository.removeOngoingFromCache(finishedMatch.getId());
        scoreTrackerService.untrackMatch(finishedMatch.getId());

        Match matchEntity = convertToMatchEntity(finishedMatch);
        matchRepository.saveConcluded(matchEntity);
    }

    private Match convertToMatchEntity(TennisMatch tennisMatch) {
        // Conversion logic here
        return new Match(); // Placeholder for the conversion logic
    }

    private Player getOrCreatePlayerByName(String name) {
        return playerDao.findByName(name)
                .orElseGet(() -> {
                    Player player = new Player();
                    player.setName(name);
                    player.setImagePath(ImageService.DEFAULT_PLAYER_IMAGE_PATH);

                    return player;
                });
    }

    private void registerPlayers(TennisMatch newMatch, MatchPayload payload) {
        var name1 = payload.playerOneName();
        var name2 = payload.playerTwoName();

        // todo: extract this validation on dto level
        validateDifferentPlayerNames(name1, name2);
        validatePlayerNotInOngoingMatch(name1, name2);

        var playerOne = getOrCreatePlayerByName(payload.playerOneName());
        var playerTwo = getOrCreatePlayerByName(payload.playerTwoName());

        newMatch.setPlayerOne(playerOne.getName());
        newMatch.setPlayerTwo(playerTwo.getName());
    }

    private static void validateDifferentPlayerNames(String name1, String name2) {
        if (name1.equals(name2)) {
            throw new PlayerRegistrationException("The names of the match participants should be different");
        }
    }

    private void validatePlayerNotInOngoingMatch(String name1, String name2) {
        // todo: good to cache player in hashset
        matchRepository.findAllOngoing().stream()
                .filter(m -> m.getPlayerOneName().equals(name1) || m.getPlayerOneName().equals(name2) ||
                             m.getPlayerTwoName().equals(name1) || m.getPlayerTwoName().equals(name2))
                .findAny()
                .ifPresent(m -> {
                    throw new PlayerRegistrationException(
                            "Player already takes participation in match with id: %s".formatted(m.getId()));
                });
    }
}
