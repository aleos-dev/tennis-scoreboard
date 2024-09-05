package com.aleos.repository;

import com.aleos.model.entity.Match;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.NonNull;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

public class MatchDao extends CrudDao<Match, UUID> {

    public MatchDao(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory, Match.class);
    }

    public List<Match> findByPlayerPatternBefore(String playerName, Instant before, int offset, int limit) {
        String findByPlayerNameHql = """
                FROM Match
                WHERE   (LOWER(playerOne.name) LIKE :name
                            OR LOWER(playerTwo.name) LIKE :name)
                        AND concludedAt < :before
                ORDER BY instant DESC
                """;

        return runWithinTxAndReturn(entityManager -> {
            TypedQuery<Match> query = entityManager.createQuery(findByPlayerNameHql, Match.class);
            query.setParameter("name", '%' + playerName.toLowerCase(Locale.ROOT) + '%');
            query.setParameter("before", before);
            query.setFirstResult(offset);
            query.setMaxResults(limit);

            return query.getResultList();

        });
    }

    public List<Match> findAllBefore(Instant before, int offset, int limit) {
        String findAllConcludedMatchesHql = """
                FROM Match
                WHERE concludedAt < :before
                ORDER by instant DESC
                """;

        return runWithinTxAndReturn(entityManager -> {
            TypedQuery<Match> query =
                    entityManager.createQuery(findAllConcludedMatchesHql, Match.class);
            query.setParameter("before", before);
            query.setFirstResult(offset);
            query.setMaxResults(limit);

            return query.getResultList();
        });
    }

    public int countAllBefore(Instant instant) {
        String countAllAfterHql = """
                SELECT COUNT(m)
                FROM Match m
                WHERE concludedAt < :instant
                """;

        return runWithinTxAndReturn(entityManager -> {
            TypedQuery<Long> query = entityManager.createQuery(countAllAfterHql, Long.class);
            query.setParameter("instant", instant);

            return query.getSingleResult().intValue();
        });
    }

    public int countAllByIdBefore(Long playerId, Instant instant) {
        String countAllAfterHql = """
                SELECT COUNT(m)
                FROM Match m
                WHERE concludedAt < :instant AND (playerOne.id = :playerId OR playerTwo.id = :playerId)
                """;

        return runWithinTxAndReturn(entityManager -> {
            TypedQuery<Long> query = entityManager.createQuery(countAllAfterHql, Long.class);
            query.setParameter("instant", instant);
            query.setParameter("playerId", playerId);

            return query.getSingleResult().intValue();
        });
    }

    public Optional<Match> findByIdFetchHistory(@NonNull UUID id) {
        String findByIdFetchHistoryHql = """
                SELECT  m
                FROM Match m
                LEFT OUTER JOIN FETCH m.info.historyEntries
                WHERE m.id = :id
                """;
        return runWithinTxAndReturn(entityManager -> {
            TypedQuery<Match> query = entityManager.createQuery(findByIdFetchHistoryHql, Match.class);
            query.setParameter("id", id);

            return query.getResultList().stream().findFirst();
        });
    }
}