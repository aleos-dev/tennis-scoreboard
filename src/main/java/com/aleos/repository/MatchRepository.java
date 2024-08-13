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

    public List<TennisMatch> findAllOngoingBefore(Instant before, int offset, int limit) {
        return ongoingMatchCache.getAll().stream()
                .filter(match -> match.getCreatedAt().isBefore(before))
                .skip(offset)
                .limit(limit)
                .toList();
    }

    public void saveConcluded(Match match) {
        matchDao.save(match);
    }

    public Optional<Match> findConcluded(@NonNull UUID id) {
        return matchDao.findById(id);
    }

    public List<Match> findAllConcludedBefore(Instant before, int offset, int limit) {
        return matchDao.findAllBefore(before, offset, limit);
    }

    public void removeOngoingFromCache(UUID id) {
        ongoingMatchCache.remove(id);
    }

    public void removeConcluded(UUID id) {
        matchDao.delete(id);
    }

    public int countAllOngoing() {
        return ongoingMatchCache.matchesCount();
    }

    public int countAllConcluded(Instant instant) {
        return matchDao.countAllBefore(instant);
    }
}