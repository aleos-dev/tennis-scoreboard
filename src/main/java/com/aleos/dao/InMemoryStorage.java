package com.aleos.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InMemoryStorage<T, K> {

    Optional<T> get(UUID id);

    List<T> getAll();

    void put(T match);

    void remove(K id);
}
