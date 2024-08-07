package com.aleos.servicelocator;

import com.aleos.annotation.Bean;
import com.aleos.exception.BeanInitializationException;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactory implements ServiceLocator {

    private final Map<String, Object> beans = new ConcurrentHashMap<>();

    public BeanFactory(Class<?> configClass) {
        initializeBeans(configClass);
    }

    public Object getBean(String beanName) {
        return beans.get(beanName);
    }

    public void registerBean(String beanName, Object serviceInstance) {
        if (beans.containsKey(beanName)) {
            throw new BeanInitializationException(String.format("The bean with name: %s is already registered.", beanName));
        }

        beans.put(beanName, serviceInstance);
    }

    private void initializeBeans(Class<?> configClass) {
        try {
            Object configInstance = configClass.getDeclaredConstructor().newInstance();

            Deque<Method> methods = new ArrayDeque<>(Arrays.stream(configClass.getDeclaredMethods()).toList());

            int retriesCount = 0;

            while (!methods.isEmpty()) {
                var method = methods.poll();
                var beanAnnotation = method.getAnnotation(Bean.class);
                if (beanAnnotation == null) {
                    continue;
                }
                String beanName = beanAnnotation.name().isEmpty() ? method.getName() : beanAnnotation.name();

                var params = resolveMethodParameters(method);

                if (params.isEmpty()) {
                    if (isCycle(++retriesCount, methods.size())) {
                        throw new BeanInitializationException("Failed to initialize the next bean: " + beanName);
                    }
                    methods.add(method);
                } else {
                    retriesCount = 0;
                    Object bean = method.invoke(configInstance, params.get());
                    registerBean(beanName, bean);
                }
            }
        } catch (Exception e) {
            throw new BeanInitializationException("Failed to initialize beans", e);
        }
    }

    private static boolean isCycle(int retriesCount, int methodsCount) {
        return retriesCount > methodsCount;
    }

    private Optional<Object[]> resolveMethodParameters(Method method) {
        Parameter[] parameters = method.getParameters();
        Object[] params = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            var beanAnnotation = parameters[i].getAnnotation(Bean.class);
            if (beanAnnotation == null) {
                throw new BeanInitializationException(
                        "Bean name should be declared for the parameter: %s of the method: %s".formatted(parameters[i], method.getName()));
            }
            String beanName = beanAnnotation.name();

            var bean = beans.get(beanName);
            if (bean == null) {
                return Optional.empty();
            }
            params[i] = bean;
        }
        return Optional.of(params);
    }
}
