package com.aleos.servicelocator;

public interface ServiceLocator {

    Object getBean(String serviceName);

    void registerBean(String serviceName, Object serviceInstance);
}
