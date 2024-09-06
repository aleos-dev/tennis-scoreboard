package com.aleos.repository;

import com.aleos.model.entity.Match;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.NonNull;

import java.util.Optional;
import java.util.UUID;

public class MatchDao extends CrudDao<Match, UUID> {

    public MatchDao(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory, Match.class);
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