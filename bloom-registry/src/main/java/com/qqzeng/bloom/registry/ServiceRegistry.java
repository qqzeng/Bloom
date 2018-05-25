package com.qqzeng.bloom.registry;

/**
 * Service registry center interface.
 * </p>
 * Created by qqzeng.
 */
public interface ServiceRegistry {
    /**
     * registry service Name associated with service Address.
     * @param serviceName
     * @param serviceAddress
     * @return registry result.
     */
    Object register(String serviceName, String serviceAddress);
}
