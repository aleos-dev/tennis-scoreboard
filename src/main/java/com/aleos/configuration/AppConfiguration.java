package com.aleos.configuration;

import com.aleos.annotation.Bean;
import com.aleos.mapper.MatchMapper;
import com.aleos.mapper.PlayerMapper;
import com.aleos.repository.*;
import com.aleos.service.ImageService;
import com.aleos.service.MatchService;
import com.aleos.service.PlayerService;
import com.aleos.service.ScoreTrackerService;
import com.aleos.util.PropertiesUtil;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.flywaydb.core.Flyway;
import org.modelmapper.ModelMapper;

public class AppConfiguration {

    @Bean
    public ValidatorFactory validationFactory() {
        return Validation.buildDefaultValidatorFactory();
    }

    @Bean
    public Validator validator(@Bean(name = "validationFactory") ValidatorFactory validationFactory) {
        return validationFactory.getValidator();
    }

    @Bean
    public PlayerDao playerDao(@Bean(name = "entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new PlayerDao(entityManagerFactory);
    }

    @Bean
    public MatchRepository matchRepository(@Bean(name = "matchDao") MatchDao matchDao,
                                           @Bean(name = "playerDao") PlayerDao playerDao) {
        return new MatchRepository(new TennisMatchCache(), matchDao, playerDao);
    }

    @Bean
    public MatchService matchService(@Bean(name = "matchRepository") MatchRepository matchRepository,
                                     @Bean(name = "playerRepository") PlayerRepository playerRepository,
                                     @Bean(name = "scoreTrackerService") ScoreTrackerService scoreTrackerService,
                                     @Bean(name = "matchMapper") MatchMapper matchMapper) {
        return new MatchService(matchRepository, playerRepository, scoreTrackerService, matchMapper);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public MatchMapper matchMapper(@Bean(name = "modelMapper") ModelMapper modelMapper,
                                   @Bean(name = "scoreTrackerService") ScoreTrackerService scoreTrackerService) {
        return new MatchMapper(modelMapper, scoreTrackerService);
    }

    @Bean
    public ScoreTrackerService scoreTrackerService() {
        return new ScoreTrackerService();
    }

    @Bean
    public MatchDao matchDao(@Bean(name = "entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new MatchDao(entityManagerFactory);
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        Flyway flyway = Flyway.configure().dataSource(
                PropertiesUtil.get("database.url").orElseThrow(),
                PropertiesUtil.get("database.user").orElseThrow(),
                PropertiesUtil.get("database.password").orElseThrow()
        ).load();

        flyway.migrate();

        return Persistence.createEntityManagerFactory(
                PropertiesUtil.get("hibernate.persistence.unit.name").orElse("default"));
    }

    @Bean
    public ImageService imageService() {
        return new ImageService();
    }

    @Bean
    public PlayerMapper playerMapper(
            @Bean(name = "modelMapper") ModelMapper mapper,
            @Bean(name = "matchService") MatchService matchService
    ) {
        return new PlayerMapper(mapper, matchService);
    }

    @Bean
    public PlayerService playerService(
            @Bean(name = "playerRepository") PlayerRepository playerRepository,
            @Bean(name = "playerMapper") PlayerMapper mapper
    ) {
        return new PlayerService(playerRepository, mapper);
    }

    @Bean
    public PlayerRepository playerRepository(@Bean(name = "playerDao") PlayerDao playerDao) {
        return new PlayerRepository(playerDao);
    }
}

