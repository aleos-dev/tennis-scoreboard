package com.aleos.repository;

import com.aleos.match.stage.TennisMatch;
import jakarta.validation.constraints.NotNull;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TennisMatchCache implements InMemoryStorage<TennisMatch, UUID> {

    private final LinkedHashMap<UUID, TennisMatch> cache = new LinkedHashMap<>();

    private final Set<String> participants = new HashSet<>();

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public Optional<TennisMatch> get(UUID id) {
        lock.readLock().lock();
        try {
            return Optional.ofNullable(cache.get(id));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<TennisMatch> getAll() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(cache.values());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void put(TennisMatch match) {
        lock.writeLock().lock();
        try {
            validateParticipant(match);

            UUID id = match.getId();
            cache.put(id, match);
            addParticipants(match);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void remove(UUID id) {
        lock.writeLock().lock();
        try {
            cache.remove(id);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public int size() {
        lock.readLock().lock();
        try {
            return cache.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean checkCurrentParticipant(String name) {
        lock.readLock().lock();
        try {
            return participants.contains(name);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Optional<UUID> findOngoingMatchIdByPlayerName(@NotNull String name) {
        return getAll().stream()
                .filter(match -> name.equals(match.getPlayerOneName()) || name.equals(match.getPlayerTwoName()))
                .map(TennisMatch::getId)
                .findAny();
    }

    private void validateParticipant(TennisMatch match) {
        if (checkCurrentParticipant(match.getPlayerOneName()) ||
            checkCurrentParticipant(match.getPlayerTwoName())) {
            throw new IllegalStateException("Participant is already involved in an ongoing match");
        }
    }

    private void addParticipants(TennisMatch match) {
        participants.add(match.getPlayerOneName());
        participants.add(match.getPlayerTwoName());
    }
}
