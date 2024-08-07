package com.aleos.configuration;

import com.aleos.service.MatchService;
import com.aleos.servicelocator.BeanFactory;
import jakarta.validation.Validator;

public class Main {

    public static void main(String[] args) {
        var factory = new BeanFactory(AppConfiguration.class);

        var matchService = (MatchService) factory.getBean("matchService");
        System.out.println(matchService);
        var validator = (Validator) factory.getBean("validator");
        System.out.println(validator);
        System.out.println();
    }

}
