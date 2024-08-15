package com.aleos.repository;

import com.aleos.match.stage.TennisMatch;
import com.aleos.model.entity.Match;
import com.aleos.model.entity.Player;
import com.aleos.model.in.MatchFilterCriteria;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.*;

@RequiredArgsConstructor
public class MatchRepository {

    private final InMemoryStorage<TennisMatch, UUID> ongoingMatchCache;

    private final MatchDao matchDao;

    private final PlayerDao playerDao;

    public void cacheOngoing(@NonNull TennisMatch ongoingMatch) {
        ongoingMatchCache.put(ongoingMatch);
    }

    public Optional<TennisMatch> findOngoing(@NonNull UUID id) {
        return ongoingMatchCache.get(id);
    }

    public List<TennisMatch> findAllOngoingByCriteria(int size,
                                                      int offset,
                                                      @NonNull Instant before,
                                                      @NonNull MatchFilterCriteria filterCriteria) {

        String status = filterCriteria.status();
        if (!("any".equalsIgnoreCase(status) || "ongoing".equalsIgnoreCase(status))) {
            return Collections.emptyList();
        }

        if (size == 0) {
            size = Integer.MAX_VALUE;
        }

        String nameCriteria = filterCriteria.playerName();
        if (nameCriteria == null) {
            return ongoingMatchCache.getAll().stream()
                    .filter(match -> match.getCreatedAt().isBefore(before))
                    .skip(offset)
                    .limit(size)
                    .toList();

        }

        return ongoingMatchCache.getAll().stream()
                .filter(match -> match.getCreatedAt().isBefore(before))
                .filter(match -> match.getPlayerOneName().equalsIgnoreCase(nameCriteria)
                                 || match.getPlayerTwoName().equalsIgnoreCase(nameCriteria))
                .skip(offset)
                .limit(size)
                .toList();
    }


    public List<Match> findAllConcludedByCriteria(int size,
                                                  int offset,
                                                  @NonNull String sortBy,
                                                  @NonNull String sortDirection,
                                                  @NonNull Instant timestamp,
                                                  @NonNull MatchFilterCriteria filterCriteria) {
        String status = filterCriteria.status();
        if (!("any".equalsIgnoreCase(status) || "finished".equalsIgnoreCase(status))) {
            return Collections.emptyList();
        }
        Optional<Player> playerOpt;

        if (filterCriteria.playerName() != null) {
            playerOpt = playerDao.findByName(filterCriteria.playerName().toLowerCase(Locale.ROOT));

            if (playerOpt.isEmpty()) {
                return Collections.emptyList();
            }

        } else {playerOpt = Optional.empty();}

        return matchDao.runWithinTxAndReturn(entityManager -> {

            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Match> cq = cb.createQuery(Match.class);
            Root<Match> matchRoot = cq.from(Match.class);

            Order order = sortDirection.equalsIgnoreCase("desc")
                    ? cb.desc(matchRoot.get(sortBy))
                    : cb.asc(matchRoot.get(sortBy));
            cq.orderBy(order);

            Predicate timestampPredicate = cb.lessThan(matchRoot.get("concludedAt"), timestamp);

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(timestampPredicate);

            if (playerOpt.isPresent()) {
                Predicate playerOnePredicate = cb.equal(matchRoot.get("playerOne"), playerOpt.get());
                Predicate playerTwoPredicate = cb.equal(matchRoot.get("playerTwo"), playerOpt.get());
                Predicate playerIdPredicate = cb.or(playerOnePredicate, playerTwoPredicate);
                predicates.add(playerIdPredicate);
            }

            cq.where(cb.and(predicates.toArray(new Predicate[0])));

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

    public int countAllOngoing(MatchFilterCriteria filterCriteria) {
        if ("any".equalsIgnoreCase(filterCriteria.status()) || "ongoing".equalsIgnoreCase(filterCriteria.status())) {

            if (filterCriteria.playerName() == null) {
                return ongoingMatchCache.matchesCount();
            } else {
                return ongoingMatchCache.checkCurrentParticipant(filterCriteria.playerName()) ? 1 : 0;
            }
        }
        return 0;
    }

    public boolean isPlayerInOngoingMatch(String playerName) {
        return ongoingMatchCache.checkCurrentParticipant(playerName);
    }

    public int countAllConcluded(Instant instant, MatchFilterCriteria filterCriteria) {
        if ("any".equalsIgnoreCase(filterCriteria.status()) || "finished".equalsIgnoreCase(filterCriteria.status())) {
            var playerName = filterCriteria.playerName();

            return playerName == null
                    ? matchDao.countAllBefore(instant)
                    : playerDao.findByName(filterCriteria.playerName().toLowerCase(Locale.ROOT))
                    .map(player -> matchDao.countAllByIdBefore(player.getId(), instant))
                    .orElse(0);
        }

        return 0;
    }
}