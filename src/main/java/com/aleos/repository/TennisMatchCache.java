package com.aleos.repository;

import com.aleos.match.stage.TennisMatch;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TennisMatchCache implements InMemoryStorage<TennisMatch, UUID> {

    private final Map<UUID, TennisMatch> cache = new ConcurrentHashMap<>();

    private final LinkedHashMap<UUID, TennisMatch> orderedCache = new LinkedHashMap<>();

    public Optional<TennisMatch> get(UUID id) {
        return Optional.ofNullable(cache.get(id));
    }

    public List<TennisMatch> getAll() {
        return cache.values().stream().toList();
    }

    public synchronized void put(TennisMatch match) {
        UUID id = match.getId();
        cache.put(id, match);
        orderedCache.put(id, match);
    }

    public synchronized void remove(UUID id) {
        cache.remove(id);
        orderedCache.remove(id);
    }

    public synchronized List<TennisMatch> getOldest(int count) {
        return orderedCache.values().stream()
                .limit(count)
                .toList();
    }
}
