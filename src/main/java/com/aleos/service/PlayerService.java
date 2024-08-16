package com.aleos.service;

import com.aleos.exceptions.InvalidPageableRequest;
import com.aleos.mapper.PlayerMapper;
import com.aleos.model.entity.Player;
import com.aleos.model.in.PageableInfo;
import com.aleos.model.in.PlayerFilterCriteria;
import com.aleos.model.in.PlayerNamePayload;
import com.aleos.model.in.PlayerPayload;
import com.aleos.model.out.PlayerDto;
import com.aleos.model.out.PlayersDto;
import com.aleos.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository repository;

    private final PlayerMapper mapper;

    public PlayerDto createPlayer(PlayerPayload payload) {
        Player entity = mapper.toEntity(payload);
        repository.createPlayer(entity);

        return mapper.toDto(entity);
    }

    public PlayersDto findAll(PageableInfo pageable, PlayerFilterCriteria filterCriteria) {

        var totalCount = repository.getCount(filterCriteria);

        var playerDtos = repository.findAll(pageable, filterCriteria).stream()
                .map(mapper::toDto)
                .toList();

        int totalPages = (int) Math.ceil(totalCount * 1.0 / pageable.getPageSize());
        if (totalPages != 0 && pageable.getPageNumber() > totalPages) {
            throw new InvalidPageableRequest("There are only %d total pages".formatted(totalPages));
        }
        boolean hasNext = pageable.getPageNumber() < totalPages;
        boolean hasPrevious = pageable.getPageNumber() > 1;

        return new PlayersDto(
                playerDtos,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                totalPages,
                totalCount,
                hasNext,
                hasPrevious
        );
    }

    public List<PlayerDto> findByName(PlayerNamePayload payload) {
        String countryCode = null;
        String playerName = payload.name();
        Instant instant = Instant.now();
        var criteria = new PlayerFilterCriteria(countryCode, playerName, instant);

        return repository.findByCriteria(criteria).stream()
                .map(mapper::toDto)
                .toList();
    }
}
