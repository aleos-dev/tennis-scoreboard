package com.aleos.repository;

import java.util.List;
import java.util.Optional;

public interface InMemoryStorage<T, K> {

    Optional<T> get(K id);

    List<T> getAll();

    void put(T match);

    void remove(K match);

    int size();

    boolean checkCurrentParticipant(String name);
}
