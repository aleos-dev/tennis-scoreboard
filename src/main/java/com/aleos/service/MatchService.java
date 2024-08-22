package com.aleos.service;

import com.aleos.ImageService;
import com.aleos.exception.PlayerRegistrationException;
import com.aleos.exception.UnknownMatchFormat;
import com.aleos.mapper.MatchMapper;
import com.aleos.match.creation.StageFactory;
import com.aleos.match.model.enums.MatchEvent;
import com.aleos.match.stage.TennisMatch;
import com.aleos.model.MatchScore;
import com.aleos.model.dto.in.*;
import com.aleos.model.entity.Match;
import com.aleos.model.entity.MatchInfo;
import com.aleos.model.entity.Player;
import com.aleos.model.dto.out.ActiveMatchDto;
import com.aleos.model.dto.out.ConcludedMatchDto;
import com.aleos.model.dto.out.MatchDto;
import com.aleos.model.dto.out.MatchesDto;
import com.aleos.repository.MatchRepository;
import com.aleos.repository.PlayerDao;
import lombok.RequiredArgsConstructor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.*;

@RequiredArgsConstructor
public class MatchService implements PropertyChangeListener {

    private final MatchRepository matchRepository;

    private final PlayerDao playerDao;

    private final ScoreTrackerService scoreTrackerService;

    private final MatchMapper mapper;

    public UUID createMatch(MatchPayload payload) {
        TennisMatch newMatch = createNewMatch(payload.format());
        registerPlayers(newMatch, payload);
        registerNewMatch(newMatch);

        return newMatch.getId();
    }

    public MatchesDto findAll(PageableInfo pageable, MatchFilterCriteria filterCriteria) {

        int totalOngoingCount = (int) matchRepository.getOngoingCount(filterCriteria);

        List<ActiveMatchDto> ongoingMatches = matchRepository
                .findAllOngoingByCriteria(pageable, filterCriteria).stream()
                .map(mapper::toDto)
                .toList();

        List<ConcludedMatchDto> concludedMatches = Collections.emptyList();
        int remainingLimit = pageable.getPageSize() - ongoingMatches.size();

        if (remainingLimit > 0) {
            int offsetCorrection = totalOngoingCount > 0 ? Math.max(pageable.getOffset() - totalOngoingCount, 0) : 0;

            concludedMatches = matchRepository.findAllConcludedByCriteria(
                            pageable,
                            filterCriteria,
                            offsetCorrection
                    ).stream()
                    .map(mapper::toDto)
                    .toList();
        }

        List<MatchDto> allMatches = new ArrayList<>(ongoingMatches.size() + concludedMatches.size());
        allMatches.addAll(ongoingMatches);
        allMatches.addAll(concludedMatches);


        int totalConcludedCount = (int) matchRepository.getConcludedCount(filterCriteria);

        int totalItems = totalConcludedCount + totalOngoingCount;
        int totalPages = (int) Math.ceil(totalItems * 1.0 / pageable.getPageSize());
        boolean hasNext = pageable.getPageNumber() < totalPages;
        boolean hasPrevious = pageable.getPageNumber() > 1;

        return MatchesDto.of(allMatches,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                totalPages,
                totalItems,
                hasNext,
                hasPrevious
                );
    }

    public Optional<MatchDto> findById(MatchUuidPayload uuidPayload) {
        var dtoOptional = matchRepository.findOngoing(uuidPayload.id())
                .map(mapper::toDto)
                .map(MatchDto.class::cast);


        if (dtoOptional.isPresent()) {
            return dtoOptional;
        }

        return matchRepository.findConcluded(uuidPayload.id())
                .map(mapper::toDto);
    }


    public void scorePoint(MatchUuidPayload matchUuidPayload, PlayerNamePayload playerNamePayload) {
        Optional<TennisMatch> ongoingOpt = matchRepository.findOngoing(matchUuidPayload.id());
        ongoingOpt.ifPresent(match -> match.scorePoint(playerNamePayload.name()));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (MatchEvent.MATCH_WINNER.getEventName().equals((evt.getPropertyName()))) {
            TennisMatch finishedMatch = (TennisMatch) evt.getSource();
            handleFinishedMatch(finishedMatch);
        }
    }

    private void registerNewMatch(TennisMatch newMatch) {
        if (matchRepository.findOngoing(newMatch.getId()).isEmpty()) {

            newMatch.addPropertyChangeListener(new WeakReference<>(this).get());
            matchRepository.cacheOngoing(newMatch);
            scoreTrackerService.trackMatch(newMatch);
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
        Match matchEntity = convertToMatchEntity(finishedMatch);

        matchRepository.removeOngoingFromCache(finishedMatch.getId());
        scoreTrackerService.untrackMatch(finishedMatch.getId());

        matchRepository.saveConcluded(matchEntity);
    }

    private Match convertToMatchEntity(TennisMatch tennisMatch) {

        Match match = new Match();
        String winnerName = tennisMatch.getMatchWinner().orElseThrow(() ->
                new IllegalStateException("Match should be concluded, id: %s".formatted(tennisMatch.getId())));

        Player playerOne = playerDao.findByName(tennisMatch.getPlayerOneName()).orElseThrow(() ->
                new IllegalStateException("Match %s must not be started with unregistered player %s"
                        .formatted(tennisMatch.getId(), tennisMatch.getPlayerOneName())));

        Player playerTwo = playerDao.findByName(tennisMatch.getPlayerTwoName()).orElseThrow(() ->
                new IllegalStateException("Match %s must not be started with unregistered player %s"
                        .formatted(tennisMatch.getId(), tennisMatch.getPlayerTwoName())));

        match.setId(tennisMatch.getId());
        match.setPlayerOne(playerOne);
        match.setPlayerTwo(playerTwo);
        match.setWinner(playerOne.getName().equalsIgnoreCase(winnerName) ? playerOne : playerTwo);

        MatchScore matchScore = scoreTrackerService.findById(tennisMatch.getId())
                .orElseThrow(() -> new IllegalStateException("Match must be tracked until it does not persisted."));

        MatchInfo matchInfo = new MatchInfo();
        matchInfo.setFormat(tennisMatch.getMatchFormat().name());
        matchInfo.getHistoryEntries().addAll(matchScore.getHistoryEntries());
        match.setInfo(matchInfo);

        return match;
    }

    private Player getOrCreatePlayerByName(String name) {
        return playerDao.findByName(name)
                .orElseGet(() -> {
                    Player player = new Player();
                    player.setName(name);
                    player.setImagePath(ImageService.DEFAULT_PLAYER_IMAGE_PATH);

                    playerDao.save(player);
                    return player;
                });
    }

    private void registerPlayers(TennisMatch newMatch, MatchPayload payload) {
        var name1 = payload.playerOneName();
        var name2 = payload.playerTwoName();

        checkPlayersAreNotInOngoingMatch(name1, name2);

        var playerOne = getOrCreatePlayerByName(payload.playerOneName());
        var playerTwo = getOrCreatePlayerByName(payload.playerTwoName());

        newMatch.setPlayerOne(playerOne.getName());
        newMatch.setPlayerTwo(playerTwo.getName());
    }

    private void checkPlayersAreNotInOngoingMatch(String playerName1, String playerName2) {
        if (matchRepository.isPlayerInOngoingMatch(playerName1) || matchRepository.isPlayerInOngoingMatch(playerName2)) {
            throw new PlayerRegistrationException(
                    "One or both players are already participating in an ongoing match.");
        }
    }
}
