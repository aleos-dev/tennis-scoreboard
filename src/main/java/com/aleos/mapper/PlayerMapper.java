package com.aleos.mapper;

import com.aleos.ImageService;
import com.aleos.model.entity.Player;
import com.aleos.model.dto.in.PlayerPayload;
import com.aleos.model.dto.out.PlayerDto;
import org.modelmapper.ModelMapper;

public class PlayerMapper {

    private final ModelMapper mapper;

    private final ImageService imageService;

    public PlayerMapper(ModelMapper mapper, ImageService imageService) {
        this.mapper = mapper;
        this.imageService = imageService;
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
                    String matchesEndpoint = String.format("/matches?playerName=%s", source.getName());
                    String avatarImageUrl = source.getImagePath();
                    String countryImageUrl = imageService.resolveImageUrlForCountry(source.getCountry());

                    return new PlayerDto(
                            source.getName(),
                            source.getCountry(),
                            avatarImageUrl,
                            countryImageUrl,
                            matchesEndpoint
                    );
                });
    }
}
