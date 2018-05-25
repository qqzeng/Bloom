package com.qqzeng.bloom.registry;

/**
 * Service discover center interface.
 * </p>
 * Created by qqzeng.
 */
public interface ServiceDiscover {
    /**
     * Get service address by service Name.
     * @param serviceName
     * @return service address.
     */
    String discover(String serviceName);
}
