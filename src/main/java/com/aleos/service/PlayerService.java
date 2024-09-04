package com.aleos.service;

import com.aleos.exception.InvalidPageableRequest;
import com.aleos.mapper.PlayerMapper;
import com.aleos.model.dto.in.PageableInfo;
import com.aleos.model.dto.in.PlayerFilterCriteria;
import com.aleos.model.dto.in.PlayerNamePayload;
import com.aleos.model.dto.in.PlayerPayload;
import com.aleos.model.dto.out.PlayerDto;
import com.aleos.model.dto.out.PlayersDto;
import com.aleos.model.entity.Player;
import com.aleos.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;

    private final PlayerMapper mapper;

    public PlayerDto createPlayer(PlayerPayload payload) {
        Player entity = mapper.toEntity(payload);
        playerRepository.createPlayer(entity);

        return mapper.toDto(entity);
    }

    public PlayersDto findAll(PageableInfo pageable, PlayerFilterCriteria filterCriteria) {

        var totalCount = playerRepository.getCount(filterCriteria);

        var playerDtos = playerRepository.findAll(pageable, filterCriteria).stream()
                .map(mapper::toDto)
                .toList();

        int totalPages = (int) Math.ceil(totalCount * 1.0 / pageable.getPageSize());
        if (totalPages != 0 && pageable.getPageNumber() > totalPages) {
            throw new InvalidPageableRequest("There are only %d total pages".formatted(totalPages));
        }
        boolean hasNext = pageable.getPageNumber() < totalPages;
        boolean hasPrevious = pageable.getPageNumber() > 1;

        return PlayersDto.of(
                playerDtos,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                totalPages,
                totalCount,
                hasNext,
                hasPrevious
        );
    }

    public Optional<PlayerDto> findByName(PlayerNamePayload payload) {
        return playerRepository.findByName(payload.name()).map(mapper::toDto);
    }

    public void update(PlayerNamePayload identifier, PlayerPayload payload) {
        Player entity = mapper.toEntity(payload);
        playerRepository.update(identifier.name(), entity);
    }

    public List<String> getCountryCodes() {
        return playerRepository.getCountryCodes();
    }
}
