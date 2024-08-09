package com.aleos.dao;

import com.aleos.match.stage.StandardMatch;
import com.aleos.match.stage.TennisSet;
import com.aleos.model.entity.Match;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

public class MatchDao extends CrudDao<Match, UUID> {

    private final InMemoryStorage<StandardMatch<TennisSet>, UUID> cache;

    public MatchDao(EntityManagerFactory entityManagerFactory,
                    InMemoryStorage<StandardMatch<TennisSet>, UUID> cache) {
        super(entityManagerFactory, Match.class);
        this.cache = cache;
    }

    public Optional<StandardMatch<TennisSet>> findOngoingMatch(UUID id) {
        return cache.get(id);
    }

    // todo: paging??
    public List<StandardMatch<TennisSet>> findAllOngoing() {
        return cache.getAll();
    }

    public List<Match> findByPlayerName(String playerName, Instant before, int limit) {
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
            query.setMaxResults(limit);

            return query.getResultList();

        });
    }


    public List<Match> findAllConcluded(Instant before, int limit) {
        String findAllConcludedMatchesSql = """
                FROM Match
                WHERE concludedAt < :before
                ORDER by instant DESC
                """;

        return runWithinTxAndReturn(entityManager -> {
            TypedQuery<Match> query =
                    entityManager.createQuery(findAllConcludedMatchesSql, Match.class);
            query.setParameter("before", before);
            query.setMaxResults(limit);

            return query.getResultList();
        });
    }
}