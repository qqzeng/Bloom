package com.qqzeng.bloom.registry.zk;

import com.qqzeng.bloom.common.utils.PropertyReader;
import com.qqzeng.bloom.registry.ServiceRegistry;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service registry implement based on Zookeeper.
 * </p>
 * Created by qqzeng.
 */
public class ZkServiceRegistry implements ServiceRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZkServiceRegistry.class);

    private final ZkClient zkClient;

    private final String zkServers;

    private static final PropertyReader PROPERTY_READER = new PropertyReader("zookeeper.properties");
    public static final int ZK_SESSION_TIMEOUT =
            Integer.parseInt(PROPERTY_READER.getProperty("ZK_SESSION_TIMEOUT"));
    public static final String ZK_REGISTRY_PATH = PROPERTY_READER.getProperty("ZK_REGISTRY_PATH");
    public static final int ZK_CONNECTION_TIMEOUT = Integer.parseInt(PROPERTY_READER.getProperty("ZK_CONNECTION_TIMEOUT"));


    public ZkServiceRegistry(String zkServers) {
        this.zkServers = zkServers;
        this.zkClient = new ZkClient(this.zkServers, ZK_SESSION_TIMEOUT, ZK_CONNECTION_TIMEOUT);
        LOGGER.debug("Connected to zookeeper server Successfully.");
        init();
    }

    /**
     * Deleted all registered node ever recursively before starting rpc server.
     */
    private void init() {
        String registryNodePath = ZK_REGISTRY_PATH;
        if (zkClient.exists(registryNodePath)) {
            zkClient.deleteRecursive(registryNodePath);
            LOGGER.debug("Delete node : {} recursively.", registryNodePath);
        }
    }

    @Override
    public Object register(String serviceName, String serviceAddress) {
        String registryNodePath = ZK_REGISTRY_PATH;
        if (!zkClient.exists(registryNodePath)) {
            zkClient.createPersistent(registryNodePath);
            LOGGER.debug("Create registry node : {}.", registryNodePath);
        }
        String serviceNodePath = registryNodePath + "/" + serviceName;
        if (!zkClient.exists(serviceNodePath)) {
            zkClient.createPersistent(serviceNodePath);
            LOGGER.debug("Create service node : {}.", serviceNodePath);
        }
        String addressNodePath = serviceNodePath + "/" + "address-";
        if (!zkClient.exists(addressNodePath)) {
//            zkClient.createEphemeralSequential(addressNodePath, serviceAddress);
            zkClient.createPersistentSequential(addressNodePath, serviceAddress);
            LOGGER.debug("Create address node : {}.", addressNodePath);
        }
        return addressNodePath;
    }
}
