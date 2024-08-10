package com.aleos.configuration;

import com.aleos.annotation.Bean;
import com.aleos.repository.MatchDao;
import com.aleos.repository.MatchRepository;
import com.aleos.repository.PlayerDao;
import com.aleos.repository.TennisMatchCache;
import com.aleos.service.MatchService;
import com.aleos.service.ScoreTrackerService;
import com.aleos.util.PropertiesUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.flywaydb.core.Flyway;

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
    public MatchRepository matchRepository(@Bean(name = "matchDao") MatchDao matchDao) {
        return new MatchRepository(new TennisMatchCache(), matchDao);
    }

    @Bean
    public MatchService matchService(@Bean(name = "matchRepository") MatchRepository matchRepository,
                                     @Bean(name = "playerDao") PlayerDao playerDao,
                                     @Bean(name = "scoreTrackerService") ScoreTrackerService scoreTrackerService) {
        return new MatchService(matchRepository, playerDao, scoreTrackerService);
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
        Flyway flyway = Flyway.configure()
                .dataSource(
                        PropertiesUtil.get("database.url").orElseThrow(),
                        PropertiesUtil.get("database.user").orElseThrow(),
                        PropertiesUtil.get("database.password").orElseThrow()
                ).load();
        flyway.migrate();

        return Persistence.createEntityManagerFactory(
                PropertiesUtil.get("hibernate.persistence.unit.name").orElse("default"));
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}

