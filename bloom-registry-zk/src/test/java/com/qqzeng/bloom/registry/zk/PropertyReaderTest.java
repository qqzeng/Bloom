package com.qqzeng.bloom.registry.zk;

import com.qqzeng.bloom.common.utils.PropertyReader;
import org.junit.Test;

/**
 * @author qqzeng
 * @desc Test Properties load.
 */
public class PropertyReaderTest {
    @Test
    public void testPropertyReader() throws Exception {
        PropertyReader READER = new PropertyReader("zookeeper.properties");
        int ZK_SESSION_TIMEOUT =
                Integer.parseInt(READER.getProperty("ZK_SESSION_TIMEOUT"));
        String ZK_REGISTRY_PATH = READER.getProperty("ZK_REGISTRY_PATH");
        String ZK_CONNECTION_TIMEOUT = READER.getProperty("ZK_CONNECTION_TIMEOUT");

        System.out.println(ZK_REGISTRY_PATH);
        System.out.println(ZK_SESSION_TIMEOUT);
        System.out.println(ZK_CONNECTION_TIMEOUT);
    }
}
