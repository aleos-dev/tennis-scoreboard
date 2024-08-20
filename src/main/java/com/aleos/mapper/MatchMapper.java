package com.aleos.mapper;

import com.aleos.match.stage.StandardMatch;
import com.aleos.match.stage.TennisMatch;
import com.aleos.model.entity.Match;
import com.aleos.model.enums.MatchStatus;
import com.aleos.model.dto.out.ActiveMatchDto;
import com.aleos.model.dto.out.ConcludedMatchDto;
import com.aleos.service.ScoreTrackerService;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.NoSuchElementException;

public class MatchMapper {

    private final ModelMapper mapper;

    private final ScoreTrackerService scoreTrackerService;

    public MatchMapper(ModelMapper mapper, ScoreTrackerService scoreTrackerService) {
        this.mapper = mapper;
        this.scoreTrackerService = scoreTrackerService;
        setupMapper();
    }

    public ActiveMatchDto toDto(TennisMatch tennisMatch) {
        return mapper.map(tennisMatch, ActiveMatchDto.class);
    }

    public ConcludedMatchDto toDto(Match match) {
        return mapper.map(match, ConcludedMatchDto.class);
    }

    private void setupMapper() {
        setConverterFromStandardMatchToActiveMatchDto();
        setConverterFromMatchToConcludedMatchDto();
    }

    private void setConverterFromStandardMatchToActiveMatchDto() {
        mapper.createTypeMap(StandardMatch.class, ActiveMatchDto.class)
                .setConverter(context -> {
                    TennisMatch source = context.getSource();

                    return new ActiveMatchDto(
                            source.getId(),
                            source.getPlayerOneName(),
                            source.getPlayerTwoName(),
                            MatchStatus.ONGOING,
                            scoreTrackerService.findById(source.getId())
                                    .orElseThrow(() -> new NoSuchElementException("Score not found for match ID: " + source.getId())),
                            LocalDateTime.ofInstant(source.getCreatedAt(), ZoneId.systemDefault())
                    );
                });
    }

    private void setConverterFromMatchToConcludedMatchDto() {
        mapper.createTypeMap(Match.class, ConcludedMatchDto.class)
                .setConverter(context -> {
                    Match source = context.getSource();

                    return new ConcludedMatchDto(
                            source.getId(),
                            source.getPlayerOne().getName(),
                            source.getPlayerTwo().getName(),
                            MatchStatus.FINISHED,
                            source.getWinner().getName(),
                            source.getInfo(),
                            LocalDateTime.ofInstant(source.getConcludedAt(), ZoneId.systemDefault())
                    );
                });
    }
}
