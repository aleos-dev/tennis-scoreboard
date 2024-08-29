package com.aleos.mapper;

import com.aleos.ImageService;
import com.aleos.model.entity.Player;
import com.aleos.model.dto.in.PlayerPayload;
import com.aleos.model.dto.out.PlayerDto;
import com.aleos.service.MatchService;
import org.modelmapper.ModelMapper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class PlayerMapper {

    private final ModelMapper mapper;

    private final ImageService imageService;

    private final MatchService matchService;

    public PlayerMapper(ModelMapper mapper, ImageService imageService, MatchService matchService) {
        this.mapper = mapper;
        this.imageService = imageService;
        this.matchService = matchService;
        configureMappings();
    }

    public PlayerDto toDto(Player player) {
        return mapper.map(player, PlayerDto.class);
    }

    public Player toEntity(PlayerPayload payload) {
        Player player = new Player();
        player.setName(payload.name());
        player.setCountry(payload.country());
        player.setImagePath(payload.imageUrl());

        return player;
    }

    private void configureMappings() {
        setConverterFromPlayerToPlayerDto();
    }

    private void setConverterFromPlayerToPlayerDto() {
        mapper.createTypeMap(Player.class, PlayerDto.class)
                .setConverter(context -> {
                    Player source = context.getSource();

                    String encodedName = URLEncoder.encode(source.getName(), StandardCharsets.UTF_8);
                    String matchesEndpoint = String.format("/matches?playerName=%s", encodedName);
                    String avatarImageUrl = source.getImagePath();
                    String countryImageUrl = imageService.resolveImageUrlForCountry(source.getCountry());
                    String ongoingMatchUuid = matchService.findOngoingMatchIdByPlayerName(source.getName())
                            .map(UUID::toString)
                            .orElse(null);

                    return new PlayerDto(
                            source.getName(),
                            source.getCountry(),
                            avatarImageUrl,
                            countryImageUrl,
                            matchesEndpoint,
                            ongoingMatchUuid
                    );
                });
    }
}
