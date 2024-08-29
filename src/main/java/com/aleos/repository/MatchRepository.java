package com.aleos.repository;

import com.aleos.match.stage.TennisMatch;
import com.aleos.model.entity.Match;
import com.aleos.model.dto.in.MatchFilterCriteria;
import com.aleos.model.dto.in.Pageable;
import com.aleos.model.dto.in.PageableInfo;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class MatchRepository {

    private final InMemoryStorage<TennisMatch, UUID> ongoingMatchCache;

    private final MatchDao matchDao;

    private final PlayerDao playerDao;

    public void cacheOngoing(@NonNull TennisMatch ongoingMatch) {
        ongoingMatchCache.put(ongoingMatch);
    }

    public void saveConcluded(Match match) {
        matchDao.save(match);
    }

    public List<TennisMatch> findAllOngoingByCriteria(@NonNull PageableInfo pageable,
                                                      @NonNull MatchFilterCriteria filterCriteria) {
        if (!isMatchingStatusCriteria(filterCriteria, "ongoing")) {
            return Collections.emptyList();
        }

        Stream<TennisMatch> matchStream = ongoingMatchCache.getAll().stream()
                .filter(match -> isBeforeInstant(match.getCreatedAt(), filterCriteria))
                .filter(match -> isPlayerNameMatchingCriteria(match, filterCriteria));

        if (pageable.getOffset() > 0) {
            matchStream = matchStream.skip(pageable.getOffset());
        }

        if (pageable.getPageSize() > 0) {
            matchStream = matchStream.limit(pageable.getPageSize());
        }

        return matchStream.toList();
    }

    public List<Match> findAllConcludedByCriteria(@NonNull PageableInfo pageable,
                                                  @NonNull MatchFilterCriteria filterCriteria,
                                                  int offsetCorrection) {
        if (!isMatchingStatusCriteria(filterCriteria, "finished")) {
            return Collections.emptyList();
        }

        return matchDao.runWithinTxAndReturn(entityManager -> {

            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Match> cq = cb.createQuery(Match.class);
            Root<Match> matchRoot = cq.from(Match.class);

            Order order = buildSortOrder(cb, matchRoot, pageable);
            cq.orderBy(order);

            List<Predicate> predicates = buildPredicates(cb, matchRoot, filterCriteria);
            cq.where(cb.and(predicates.toArray(new Predicate[0])));

            TypedQuery<Match> query = entityManager.createQuery(cq);

            applyPagination(query, pageable, offsetCorrection);

            return query.getResultList();
        });
    }

    public long getOngoingCount(MatchFilterCriteria filterCriteria) {
        if (isMatchingStatusCriteria(filterCriteria, "ongoing")) {
            return ongoingMatchCache.getAll().stream()
                    .filter(match -> isBeforeInstant(match.getCreatedAt(), filterCriteria))
                    .filter(match -> isPlayerNameMatchingCriteria(match, filterCriteria))
                    .count();
        }
        return 0;
    }

    public long getConcludedCount(MatchFilterCriteria filterCriteria) {

        if (isMatchingStatusCriteria(filterCriteria, "finished")) {
            return matchDao.runWithinTxAndReturn(
                    entityManager -> {

                        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
                        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
                        Root<Match> matchRoot = cq.from(Match.class);

                        cq.select(cb.count(matchRoot));

                        List<Predicate> predicates = buildPredicates(cb, matchRoot, filterCriteria);
                        cq.where(cb.and(predicates.toArray(new Predicate[0])));

                        TypedQuery<Long> query = entityManager.createQuery(cq);

                        return query.getSingleResult();
                    });
        }
        return 0;
    }

    public Optional<TennisMatch> findOngoing(@NonNull UUID id) {
        return ongoingMatchCache.get(id);
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


    private boolean isPlayerNameMatchingCriteria(TennisMatch match, MatchFilterCriteria filterCriteria) {
        return filterCriteria.playerName() == null
               || match.getPlayerOneName().equalsIgnoreCase(filterCriteria.playerName())
               || match.getPlayerTwoName().equalsIgnoreCase(filterCriteria.playerName());

    }

    private boolean isMatchingStatusCriteria(MatchFilterCriteria filterCriteria, String... validStatuses) {
        String status = filterCriteria.status();
        return status == null || "any".equalsIgnoreCase(status) || Arrays.stream(validStatuses).anyMatch(status::equalsIgnoreCase);
    }


    private boolean isBeforeInstant(Instant instant, MatchFilterCriteria filterCriteria) {
        return filterCriteria.before() == null || instant.isBefore(filterCriteria.before());
    }


    public boolean isPlayerInOngoingMatch(String playerName) {
        return ongoingMatchCache.checkCurrentParticipant(playerName);
    }

    public Optional<UUID> findOngoingMatchIdByPlayerName(String playerName) {
        return ongoingMatchCache.findOngoingMatchIdByPlayerName(playerName);
    }


    private Order buildSortOrder(CriteriaBuilder cb, Root<Match> matchRoot, Pageable pageable) {
        String timestamp = "concludedAt";
        String sortBy = pageable.getSortBy().orElse(timestamp);
        return pageable.getSortDirection().equalsIgnoreCase("DESC")
                ? cb.desc(matchRoot.get(sortBy))
                : cb.asc(matchRoot.get(sortBy));
    }

    private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<Match> matchRoot, MatchFilterCriteria
            filterCriteria) {
        List<Predicate> predicates = new ArrayList<>();

        if (filterCriteria.playerName() != null) {
            List<Long> matchingPlayerIds = playerDao.findIdsByNamePattern(filterCriteria.playerName());

            if (!matchingPlayerIds.isEmpty()) {

                Predicate playerOnePredicate = matchRoot.get("playerOne").get("id").in(matchingPlayerIds);
                Predicate playerTwoPredicate = matchRoot.get("playerTwo").get("id").in(matchingPlayerIds);

                predicates.add(cb.or(playerOnePredicate, playerTwoPredicate));
            } else {
                predicates.add(cb.disjunction());
            }
        }

        if (filterCriteria.before() != null) {
            Predicate timestampPredicate = cb.lessThan(matchRoot.get("concludedAt"), filterCriteria.before());
            predicates.add(timestampPredicate);
        }

        return predicates;
    }

    private void applyPagination(TypedQuery<Match> query, Pageable pageable, int offsetCorrection) {
        int offset = Math.max(pageable.getOffset() + offsetCorrection, 0);
        if (offset >  0) {
            query.setFirstResult(offset);
        }

        int size = pageable.getPageSize();
        if (size != 0) {
            query.setMaxResults(size);
        }
    }
}