package com.aleos.mapper;

import com.aleos.match.stage.TennisMatch;
import com.aleos.model.entity.Match;
import com.aleos.model.enums.MatchStatus;
import com.aleos.model.out.ActiveMatchDto;
import com.aleos.model.out.ConcludedMatchDto;
import com.aleos.service.ScoreTrackerService;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.NoSuchElementException;


public class MatchMapper {

    private final ModelMapper mapper;

    private final ScoreTrackerService scoreTrackerService;

    public MatchMapper(ModelMapper mapper, ScoreTrackerService scoreTrackerService) {
        this.mapper = mapper;
        this.scoreTrackerService = scoreTrackerService;
        configureMappings();
    }

    private void configureMappings() {

        Converter<TennisMatch, ActiveMatchDto> toActive = context -> {
            TennisMatch source = context.getSource();
            return new ActiveMatchDto(
                    source.getId(),
                    source.getPlayerOneName(),
                    source.getPlayerTwoName(),
                    MatchStatus.ONGOING,
                    scoreTrackerService.findById(source.getId())
                            .orElseThrow(() -> new NoSuchElementException("Score not found for match ID: " + source.getId())),
                    LocalDateTime.ofInstant(source.getCreatedAt(), ZoneId.systemDefault()).toLocalTime()
            );
        };

        Converter<Match, ConcludedMatchDto> toConcluded = context -> {
            Match source = context.getSource();
            return new ConcludedMatchDto(
                    source.getId(),
                    source.getPlayerOne().getName(),
                    source.getPlayerTwo().getName(),
                    MatchStatus.FINISHED,
                    source.getWinner().getName(),
                    source.getInfo(),
                    LocalDate.ofInstant(source.getConcludedAt(), ZoneId.systemDefault())
            );
        };

        mapper.createTypeMap(TennisMatch.class, ActiveMatchDto.class).setConverter(toActive);
        mapper.createTypeMap(Match.class, ConcludedMatchDto.class).setConverter(toConcluded);
    }

    public ActiveMatchDto convertToActiveMatchDto(TennisMatch tennisMatch) {
        return mapper.map(tennisMatch, ActiveMatchDto.class);
    }

    public ConcludedMatchDto convertToConcludedMatchDto(Match match) {
        return mapper.map(match, ConcludedMatchDto.class);
    }
}
