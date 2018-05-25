package com.qqzeng.bloom.common;

import com.qqzeng.bloom.common.utils.PropertyReader;
import org.junit.Test;

/**
 * @author qqzeng
 * @desc
 */
public class PropertyReaderTest {
    @Test
    public void testPropertyReader() throws Exception {
        PropertyReader READER = new PropertyReader("zookeeper.properties");
        int ZK_SESSION_TIMEOUT =
                Integer.parseInt(READER.getProperty("ZK_SESSION_TIMEOUT"));
        String ZK_REGISTRY_PATH = READER.getProperty("ZK_REGISTRY_PATH");

    }
}
