package com.aleos.repository;

import com.aleos.match.stage.TennisMatch;
import com.aleos.model.entity.Match;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class MatchRepository {

    private final InMemoryStorage<TennisMatch, UUID> ongoingMatchCache;
    private final MatchDao matchDao;

    public void cacheOngoing(@NonNull TennisMatch ongoingMatch) {
        ongoingMatchCache.put(ongoingMatch);
    }

    public Optional<TennisMatch> findOngoing(@NonNull UUID id) {
        return ongoingMatchCache.get(id);
    }

    public List<TennisMatch> findAllOngoing() {
        return ongoingMatchCache.getAll();
    }

    public Match saveConcluded(Match match) {
        return matchDao.save(match);
    }

    public Optional<Match> findConcluded(@NonNull UUID id) {
        return matchDao.findById(id);
    }

    public List<Match> findAllConcluded(Instant before, int limit) {
        return matchDao.findAllBefore(before, limit);
    }

    public void removeOngoingFromCache(UUID id) {
        ongoingMatchCache.remove(id);
    }

    public void removeConcluded(UUID id) {
        matchDao.delete(id);
    }
}