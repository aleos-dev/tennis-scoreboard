package com.aleos.repository;

import com.aleos.model.entity.Player;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public class PlayerDao extends CrudDao<Player, Long> {

    public PlayerDao(EntityManagerFactory emf) {
        super(emf, Player.class);
    }

    public Optional<Player> findByName(@NonNull String name) {
        String findByPlayerNameSql = """
                FROM Player
                WHERE name = :name
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

    public List<Long> findIdsByNamePattern(@NonNull String namePattern) {
        String findByPlayerNameSql = """
                SELECT id
                FROM Player
                WHERE name LIKE :queryPattern
                """;
        String queryPattern = "%" + namePattern + "%";

        return runWithinTxAndReturn(entityManager -> entityManager.createQuery(findByPlayerNameSql, Long.class)
                .setParameter("queryPattern", queryPattern)
                .getResultList());
    }
}
