package com.aleos.dao;


import com.aleos.model.entity.Player;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.time.Instant;
import java.util.List;
import java.util.Locale;

public class PlayerDao extends CrudDao<Player, Long> {

    public PlayerDao(EntityManagerFactory emf) {
        super(emf, Player.class);
    }

    public List<Player> findByName(String name, Instant after, int limit) {
        String findByPlayerNameSql = """
                FROM Player
                WHERE   (LOWER(name) LIKE :name)
                        AND createdAt > :after
                ORDER BY instant
                """;

        return runWithinTxAndReturn(entityManager -> {
            TypedQuery<Player> query = entityManager.createQuery(findByPlayerNameSql, Player.class);
            query.setParameter("name", '%' + name.toLowerCase(Locale.ROOT) + '%');
            query.setParameter("after", after);
            query.setMaxResults(limit);

            return query.getResultList();
        });
    }
}
