package com.aleos.repository;

import com.aleos.match.stage.TennisMatch;
import com.aleos.model.entity.Match;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
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

    public List<TennisMatch> findAllOngoingByCriteria(int size, int offset, Instant before) {
        if (size == 0) {
            size = Integer.MAX_VALUE;
        }

        return ongoingMatchCache.getAll().stream()
                .filter(match -> match.getCreatedAt().isBefore(before))
                .skip(offset)
                .limit(size)
                .toList();
    }


    public List<Match> findAllConcludedByCriteria(int size,
                                                  int offset,
                                                  String sortBy,
                                                  String sortDirection,
                                                  Instant timestamp) {

        return matchDao.runWithinTxAndReturn(entityManager -> {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Match> cq = cb.createQuery(Match.class);
            Root<Match> matchRoot = cq.from(Match.class);

            Order order = sortDirection.equalsIgnoreCase("desc") ?
                    cb.desc(matchRoot.get(sortBy)) :
                    cb.asc(matchRoot.get(sortBy));
            cq.orderBy(order);

            Predicate timestampPredicate = cb.lessThan(matchRoot.get("concludedAt"), timestamp);

            cq.where(timestampPredicate);

            TypedQuery<Match> query = entityManager.createQuery(cq);
            if (offset != 0) {
                query.setFirstResult(offset);
            }
            if (size != 0) {
                query.setMaxResults(size);
            }

            return query.getResultList();
        });
    }


    public void saveConcluded(Match match) {
        matchDao.save(match);
    }

    public Optional<Match> findConcluded(@NonNull UUID id) {
        return matchDao.findById(id);
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