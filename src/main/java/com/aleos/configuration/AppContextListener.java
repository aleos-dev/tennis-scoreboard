package com.aleos.configuration;

import com.aleos.service.ImageService;
import com.aleos.service.MatchService;
import com.aleos.servicelocator.BeanFactory;
import com.aleos.servicelocator.ServiceLocator;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        injectFactoryBean(sce);
        generateOngoingMatchExample(sce);
        copyInitialAvatars(sce);
    }

    private void generateOngoingMatchExample(ServletContextEvent sce) {
        var locator = (ServiceLocator) sce.getServletContext().getAttribute(AppContextAttribute.BEAN_FACTORY.name());
        var matchService = (MatchService) locator.getBean("matchService");

        matchService.generateOngoingMatchExample();
        matchService.generateOngoingMatchExample();
    }

    private void injectFactoryBean(ServletContextEvent sce) {
        var factory = new BeanFactory(AppConfiguration.class);
        sce.getServletContext().setAttribute(AppContextAttribute.BEAN_FACTORY.name(), factory);
    }

    private void copyInitialAvatars(ServletContextEvent sce) {
        var locator = (ServiceLocator) sce.getServletContext().getAttribute(AppContextAttribute.BEAN_FACTORY.name());
        var imageService = (ImageService) locator.getBean("imageService");

        imageService.deployAvatarsFromResources();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        var locator = (ServiceLocator) sce.getServletContext().getAttribute(AppContextAttribute.BEAN_FACTORY.name());
        var emf = (EntityManagerFactory) locator.getBean("entityManagerFactory");
        emf.close();
    }
}
