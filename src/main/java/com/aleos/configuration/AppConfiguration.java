package com.aleos.configuration;

import com.aleos.annotation.Bean;
import com.aleos.dao.MatchDao;
import com.aleos.dao.OngoingMatchCache;
import com.aleos.dao.PlayerDao;
import com.aleos.service.MatchService;
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
    public MatchService matchService(@Bean(name = "matchDao") MatchDao matchDao,
                                     @Bean(name = "playerDao") PlayerDao playerDao) {
        return new MatchService(matchDao, playerDao);
    }

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
    public OngoingMatchCache ongoingMatchCache() {
        return new OngoingMatchCache();
    }

    @Bean
    public MatchDao matchDao(@Bean(name = "entityManagerFactory") EntityManagerFactory entityManagerFactory,
                             @Bean(name = "ongoingMatchCache") OngoingMatchCache cache) {
        return new MatchDao(entityManagerFactory, cache);
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
                PropertiesUtil.get("hibernate.persistense.unit.name").orElse("default"));
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}

