package com.aleos.configuration;

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

    private static void injectFactoryBean(ServletContextEvent sce) {
        var factory = new BeanFactory(AppConfiguration.class);
        sce.getServletContext().setAttribute(AppContextAttribute.BEAN_FACTORY.name(), factory);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        var locator = (ServiceLocator) sce.getServletContext().getAttribute(AppContextAttribute.BEAN_FACTORY.name());
        var emf = (EntityManagerFactory) locator.getBean("entityManagerFactory");
        emf.close();
    }
}
