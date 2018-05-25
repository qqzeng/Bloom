package com.qqzeng.bloom.registry.zk;

import com.qqzeng.bloom.common.utils.PropertyReader;
import com.qqzeng.bloom.registry.ServiceDiscover;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Service discover implement based on Zookeeper.
 * </p>
 * Created by qqzeng.
 */
public class ZkServiceDiscovery implements ServiceDiscover {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZkServiceDiscovery.class);

    private final ZkClient zkClient;

    private final String zkServers;

    private static final PropertyReader PROPERTY_READER = new PropertyReader("zookeeper.properties");
    public static final int ZK_SESSION_TIMEOUT =
            Integer.parseInt(PROPERTY_READER.getProperty("ZK_SESSION_TIMEOUT"));
    public static final String ZK_REGISTRY_PATH = PROPERTY_READER.getProperty("ZK_REGISTRY_PATH");
    public static final int ZK_CONNECTION_TIMEOUT = Integer.parseInt(PROPERTY_READER.getProperty("ZK_CONNECTION_TIMEOUT"));

    public ZkServiceDiscovery(String zkServers) {
        this.zkServers = zkServers;
        this.zkClient = new ZkClient(this.zkServers, ZK_SESSION_TIMEOUT, ZK_CONNECTION_TIMEOUT);
        LOGGER.debug("Connected to zookeeper server Successfully.");
    }

    @Override
    public String discover(String serviceName) {
        String registryNodePath = ZK_REGISTRY_PATH;
        if (!zkClient.exists(registryNodePath)) {
            throw new RuntimeException(String.format("Can not find any registry node on path: %s", registryNodePath));
        }
        String serviceNodePath = registryNodePath + "/" + serviceName;
        if (!zkClient.exists(serviceNodePath)) {
            throw new RuntimeException(String.format("Can not find any service node on path: %s", serviceNodePath));
        }
        final List<String> addressNodes = zkClient.getChildren(serviceNodePath);
        if (addressNodes == null || addressNodes.size() == 0) {
            throw new RuntimeException(String.format("Can not find any address node on path: %s", serviceNodePath));
        }
        String addressRes;
        if (addressNodes.size() == 1) {
            addressRes = addressNodes.get(0);
            LOGGER.debug("Get only address node: {}", addressRes);
        } else {
            addressRes = addressNodes.get(ThreadLocalRandom.current().nextInt(addressNodes.size()));
            LOGGER.debug("Get random address node: {}", addressRes);
        }
        return zkClient.readData(serviceNodePath + "/" + addressRes);

    }
}
