package com.aleos.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InMemoryStorage<T, K> {

    Optional<T> get(K id);

    List<T> getAll();

    void put(T match);

    void remove(K match);

    int size();

    boolean checkCurrentParticipant(String name);

    Optional<UUID> findOngoingMatchIdByPlayerName(String name);

    List<String> getParticipants();
}
