package com.aleos.mapper;

import com.aleos.model.dto.in.PlayerPayload;
import com.aleos.model.dto.out.PlayerDto;
import com.aleos.model.entity.Player;
import com.aleos.service.MatchService;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class PlayerMapper {

    private final ModelMapper mapper;

    private final MatchService matchService;

    public PlayerMapper(ModelMapper mapper, MatchService matchService) {
        this.mapper = mapper;
        this.matchService = matchService;
        configureMapper();
    }

    public PlayerDto toDto(Player player) {
        return mapper.map(player, PlayerDto.class);
    }

    public Player toEntity(PlayerPayload payload) {
        var player = new Player();
        player.setName(payload.name());
        player.setCountry(payload.country());

        return player;
    }

    private void configureMapper() {
        mapper.createTypeMap(Player.class, PlayerDto.class)
                .setProvider(playerDtoProvider());
    }

    private Provider<PlayerDto> playerDtoProvider() {
        return req -> {
            var source = (Player) req.getSource();

            String encodedName = URLEncoder.encode(source.getName(), StandardCharsets.UTF_8);
            String matchesEndpoint = String.format("/matches?playerName=%s", encodedName);

            String ongoingMatchUuid = matchService.findOngoingMatchIdByPlayerName(source.getName())
                    .map(UUID::toString)
                    .orElse(null);

            return new PlayerDto(
                    source.getName(),
                    source.getCountry(),
                    matchesEndpoint,
                    ongoingMatchUuid
            );
        };
    }
}
