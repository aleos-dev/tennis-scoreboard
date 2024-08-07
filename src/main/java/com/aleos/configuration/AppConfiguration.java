package com.aleos.configuration;

import com.aleos.annotation.Bean;
import com.aleos.repository.MatchRepository;
import com.aleos.repository.PlayerRepository;
import com.aleos.service.MatchService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class AppConfiguration {

    @Bean
    public MatchService matchService(@Bean(name = "matchRepository") MatchRepository matchRepository,
                                     @Bean(name = "playerRepository") PlayerRepository playerRepository) {
        return new MatchService(matchRepository, playerRepository);
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
    public PlayerRepository playerRepository() {
        return new PlayerRepository();
    }

    @Bean
    public MatchRepository matchRepository() {
        return new MatchRepository();
    }
}
