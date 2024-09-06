package com.aleos.repository;

import com.aleos.exception.EntityNotFoundDbException;
import com.aleos.model.dto.in.Pageable;
import com.aleos.model.dto.in.PlayerFilterCriteria;
import com.aleos.model.entity.Player;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public Optional<Player> findByName(String name) {
        return playerDao.findByName(name);
    }

    public void update(String nameIdentifier, Player playerToUpdate) {
        playerDao.runWithinTx(entityManager -> {
            try {
                TypedQuery<Player> query = entityManager.createQuery("FROM Player WHERE name = :name", Player.class);
                query.setParameter("name", nameIdentifier);
                Player foundedPlayer = query.getSingleResult();

                playerToUpdate.setId(foundedPlayer.getId());
                entityManager.merge(playerToUpdate);

            } catch (NoResultException e) {
                throw new EntityNotFoundDbException("Player: %s not found.".formatted(nameIdentifier));
            }
        });
    }

    public List<String> getCountryCodes() {
        return playerDao.runWithinTxAndReturn(em -> em.createQuery("SELECT DISTINCT country FROM Player", String.class)
                .getResultList().stream()
                .sorted()
                .toList());
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

        return pageable.getSortDirection().equalsIgnoreCase("DESC")
                ? cb.desc(playerRoot.get(sortBy))
                : cb.asc(playerRoot.get(sortBy));
    }

    private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<Player> playerRoot, PlayerFilterCriteria filterCriteria) {
        List<Predicate> predicates = new ArrayList<>();

        if (filterCriteria.name() != null) {
            var nameCriteria = "%" + filterCriteria.name() + "%";
            var nameWithTitleLetter = "%" + Character.toUpperCase(filterCriteria.name().charAt(0)) + filterCriteria.name().substring(1) + "%";

            Predicate namePredicate = cb.like(playerRoot.get("name"), nameCriteria);
            Predicate nameWithTitleLetterPredicate = cb.like(playerRoot.get("name"), nameWithTitleLetter);

            Predicate combinedPredicate = cb.or(namePredicate, nameWithTitleLetterPredicate);
            predicates.add(combinedPredicate);
        }

        if (filterCriteria.country() != null) {
            Predicate countryPredicate = cb.equal(playerRoot.get("country"), filterCriteria.country());
            predicates.add(countryPredicate);
        }

        if (filterCriteria.before() != null) {
            Instant instant = filterCriteria.before();
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
