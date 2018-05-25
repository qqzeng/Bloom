package com.qqzeng.bloom.demo.server;

import com.qqzeng.bloom.registry.ServiceRegistry;
import com.qqzeng.bloom.registry.zk.ZkServiceRegistry;
import com.qqzeng.bloom.server.BloomServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BloomBootstrap {

    private static final Logger LOGGER = LoggerFactory.getLogger(BloomBootstrap.class);

    public static void main(String[] args) throws Exception{
        LOGGER.debug("Start server");
        new ClassPathXmlApplicationContext("bloom-demo-server-spring.xml");

//        ServiceRegistry serviceRegistry = new ZkServiceRegistry("192.168.0.106:2181");
//        new BloomServer("127.0.0.1:8000", serviceRegistry).serverSetup();
    }
}
