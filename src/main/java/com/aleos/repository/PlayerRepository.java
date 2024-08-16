package com.aleos.repository;

import com.aleos.model.entity.Player;
import com.aleos.model.in.Pageable;
import com.aleos.model.in.PlayerFilterCriteria;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PlayerRepository {

    private final PlayerDao playerDao;

    public void createPlayer(Player entity) {
        playerDao.save(entity);
    }

    public List<Player> findAll(Pageable pageable, PlayerFilterCriteria filterCriteria) {
        return playerDao.runWithinTxAndReturn(entityManager -> {

            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Player> cq = cb.createQuery(Player.class);
            Root<Player> playerRoot = cq.from(Player.class);

            Order order = buildSortOrder(cb, playerRoot, pageable);
            cq.orderBy(order);

            if (filterCriteria != null) {
                List<Predicate> predicates = buildPredicates(cb, playerRoot, filterCriteria);
                cq.where(cb.and(predicates.toArray(new Predicate[0])));
            }

            TypedQuery<Player> query = entityManager.createQuery(cq);

            applyPagination(query, pageable);

            return query.getResultList();
        });
    }

    public List<Player> findByCriteria(PlayerFilterCriteria filterCriteria) {
        StringBuilder queryBuilder = new StringBuilder("FROM Player WHERE 1=1");

        if (filterCriteria.name() != null) {
            queryBuilder.append(" AND name LIKE :name");
        }

        if (filterCriteria.country() != null) {
            queryBuilder.append(" AND country = :country");
        }

        if (filterCriteria.instant() != null) {
            queryBuilder.append(" AND instant < :instant");
        }

        String query = queryBuilder.toString();

        return playerDao.runWithinTxAndReturn(entityManager -> {
            var jpaQuery = entityManager.createQuery(query, Player.class);

            if (filterCriteria.name() != null) {
                jpaQuery.setParameter("name", "%" + filterCriteria.name() + "%");
            }

            if (filterCriteria.country() != null) {
                jpaQuery.setParameter("country", filterCriteria.country().toUpperCase());
            }

            return jpaQuery.getResultList();
        });
    }

    public long getCount(PlayerFilterCriteria filterCriteria) {
        return playerDao.runWithinTxAndReturn(entityManager -> {

            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Player> playerRoot = cq.from(Player.class);

            cq.select(cb.count(playerRoot));

            if (filterCriteria != null) {
                List<Predicate> predicates = buildPredicates(cb, playerRoot, filterCriteria);
                cq.where(cb.and(predicates.toArray(new Predicate[0])));
            }

            TypedQuery<Long> query = entityManager.createQuery(cq);

            return query.getSingleResult();
        });
    }

    private Order buildSortOrder(CriteriaBuilder cb, Root<Player> playerRoot, Pageable pageable) {
        String nameAttribute = "name";
        String sortBy = pageable.getSortBy().orElse(nameAttribute);
        return pageable.getSortDirection().equalsIgnoreCase("ASC")
                ? cb.desc(playerRoot.get(sortBy))
                : cb.asc(playerRoot.get(sortBy));
    }

    private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<Player> playerRoot, PlayerFilterCriteria filterCriteria) {
        List<Predicate> predicates = new ArrayList<>();

        if (filterCriteria.name() != null) {
            String nameCriteria = "%" + filterCriteria.name() + "%";
            Predicate namePredicate = cb.like(playerRoot.get("name"), nameCriteria);
            predicates.add(namePredicate);
        }

        if (filterCriteria.country() != null) {
            Predicate countryPredicate = cb.equal(playerRoot.get("country"), filterCriteria.country());
            predicates.add(countryPredicate);
        }

        if (filterCriteria.instant() != null) {
            Instant instant = filterCriteria.instant();
            Predicate timestampPredicate = cb.lessThan(playerRoot.get("createdAt"), instant);
            predicates.add(timestampPredicate);
        }

        return predicates;
    }

    private void applyPagination(TypedQuery<Player> query, Pageable pageable) {
        int offset = pageable.getOffset();
        if (offset != 0) {
            query.setFirstResult(offset);
        }

        int size = pageable.getPageSize();
        if (size != 0) {
            query.setMaxResults(size);
        }
    }

}
