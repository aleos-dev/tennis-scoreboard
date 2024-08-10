package com.aleos.repository;


import com.aleos.model.entity.Player;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.NonNull;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class PlayerDao extends CrudDao<Player, Long> {

    public PlayerDao(EntityManagerFactory emf) {
        super(emf, Player.class);
    }

    public Optional<Player> findByName(@NonNull String name) {
        String findByPlayerNameSql = """
                FROM Player
                WHERE   LOWER(name) = :name
                """;

        return runWithinTxAndReturn(entityManager -> {
            TypedQuery<Player> query = entityManager.createQuery(findByPlayerNameSql, Player.class);
            query.setParameter("name", name);

            List<Player> resultList = query.getResultList();

            return resultList.isEmpty()
                    ? Optional.empty()
                    : Optional.of(resultList.getFirst());
        });
    }

    public List<Player> findByNamePatternAfter(@NonNull String nameRegex, @NonNull Instant after, int limit) {
        String findByPlayerNameSql = """
                FROM Player
                WHERE   (LOWER(name) LIKE :name)
                        AND createdAt > :after
                ORDER BY instant
                """;

        return runWithinTxAndReturn(entityManager -> {
            TypedQuery<Player> query = entityManager.createQuery(findByPlayerNameSql, Player.class);
            query.setParameter("name", '%' + nameRegex.toLowerCase(Locale.ROOT) + '%');
            query.setParameter("after", after);
            if (limit > 0) {
                query.setMaxResults(limit);
            }

            return query.getResultList();
        });
    }
}
