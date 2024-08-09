package com.aleos.dao;

import com.aleos.match.stage.StandardMatch;
import com.aleos.match.stage.TennisSet;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class OngoingMatchCache implements InMemoryStorage<StandardMatch<TennisSet>, UUID> {

    private final Map<UUID, StandardMatch<TennisSet>> cache = new ConcurrentHashMap<>();

    public Optional<StandardMatch<TennisSet>> get(UUID id) {
        return Optional.ofNullable(cache.get(id));
    }

    public List<StandardMatch<TennisSet>> getAll() {
        return new ArrayList<>(cache.values());
    }

    public void put(StandardMatch<TennisSet> match) {
        cache.put(match.getId(), match);
    }

    public void remove(UUID id) {
        cache.remove(id);
    }
}
