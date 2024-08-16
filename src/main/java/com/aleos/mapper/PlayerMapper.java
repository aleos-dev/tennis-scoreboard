Plpackage com.aleos.mapper;

import com.aleos.ImageService;
import com.aleos.model.entity.Player;
import com.aleos.model.in.PlayerPayload;
import com.aleos.model.out.PlayerDto;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

public class PlayerMapper {

    private final ModelMapper mapper;

    private final ImageService imageService;

    public PlayerMapper(ModelMapper mapper, ImageService imageService) {
        this.mapper = mapper;
        this.imageService = imageService;
        configureMappings();
    }

    private void configureMappings() {

        Converter<Player, PlayerDto> toDto = context -> {
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
        };

        Converter<PlayerPayload, Player> toEntity = context -> {
            PlayerPayload source = context.getSource();

            Player player = new Player();
            player.setName(source.name());
            player.setCountry(source.country());
            player.setImagePath(source.imageUrl());

            return player;
        };

        mapper.createTypeMap(Player.class, PlayerDto.class).setConverter(toDto);
        mapper.createTypeMap(PlayerPayload.class, Player.class).setConverter(toEntity);
    }

    public PlayerDto toDto(Player player) {
        return mapper.map(player, PlayerDto.class);
    }

    public Player toEntity(PlayerPayload payload) {
        return mapper.map(payload, Player.class);
    }
}
