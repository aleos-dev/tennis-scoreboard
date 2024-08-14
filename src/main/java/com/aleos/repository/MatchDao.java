package com.aleos.repository;

import com.aleos.model.entity.Match;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class MatchDao extends CrudDao<Match, UUID> {

    public MatchDao(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory, Match.class);
    }

    public List<Match> findByPlayerPatternBefore(String playerName, Instant before, int offset, int limit) {
        String findByPlayerNameSql = """
                FROM Match
                WHERE   (LOWER(playerOne.name) LIKE :name
                            OR LOWER(playerTwo.name) LIKE :name)
                        AND concludedAt < :before
                ORDER BY instant DESC
                """;

        return runWithinTxAndReturn(entityManager -> {
            TypedQuery<Match> query = entityManager.createQuery(findByPlayerNameSql, Match.class);
            query.setParameter("name", '%' + playerName.toLowerCase(Locale.ROOT) + '%');
            query.setParameter("before", before);
            query.setFirstResult(offset);
            query.setMaxResults(limit);

            return query.getResultList();

        });
    }

    public List<Match> findAllBefore(Instant before, int offset, int limit) {
        String findAllConcludedMatchesSql = """
                FROM Match
                WHERE concludedAt < :before
                ORDER by instant DESC
                """;

        return runWithinTxAndReturn(entityManager -> {
            TypedQuery<Match> query =
                    entityManager.createQuery(findAllConcludedMatchesSql, Match.class);
            query.setParameter("before", before);
            query.setFirstResult(offset);
            query.setMaxResults(limit);

            return query.getResultList();
        });
    }

    public int countAllBefore(Instant instant) {
        String countAllAfterSql = """
                SELECT COUNT(m)
                FROM Match m
                WHERE concludedAt < :instant
                """;

        return runWithinTxAndReturn(entityManager -> {
            TypedQuery<Long> query = entityManager.createQuery(countAllAfterSql, Long.class);
            query.setParameter("instant", instant);

            return query.getSingleResult().intValue();
        });
    }
}