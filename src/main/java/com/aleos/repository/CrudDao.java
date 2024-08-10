package com.aleos.repository;

import com.aleos.exception.DaoOperationException;
import com.aleos.model.entity.Match;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
public abstract class CrudDao<E, K> {

    protected final EntityManagerFactory emf;

    protected final Class<E> clazz;

    public E save(@NonNull E entity) {
        runWithinTx(em -> em.persist(entity));
        return entity;
    }

    public Optional<E> findById(@NonNull K id) {
        return Optional.ofNullable(runWithinTxAndReturn(em -> em.find(clazz, id)));
    }

    public void update(@NonNull E entity, @NonNull K id) {
        runWithinTx(em -> {
            E e = em.find(clazz, id);
            if (e != null) {
                em.merge(entity);
            }
        });
    }

    public void delete(@NonNull K id) {
        runWithinTx(em -> {
            try {
                Match match = em.getReference(Match.class, id);
                em.remove(match);
            } catch (EntityNotFoundException e) {
                // should be checked as reference does not make call to db.
                throw new DaoOperationException("Match with id: %s not found".formatted(id));
            }
        });
    }

    protected <T> T runWithinTxAndReturn(Function<EntityManager, T> eMFunc) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        T result;
        try {
            result = eMFunc.apply(em);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new DaoOperationException(e.getMessage() == null ? "Transaction is rolled back." : e.getMessage(), e);
        } finally {
            em.close();
        }
        return result;
    }

    protected void runWithinTx(Consumer<EntityManager> eMFunc) {
        runWithinTxAndReturn(entityManager -> {
            eMFunc.accept(entityManager);
            return null;
        });
    }
}
