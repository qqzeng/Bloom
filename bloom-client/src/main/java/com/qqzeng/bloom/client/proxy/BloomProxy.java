package com.qqzeng.bloom.client.proxy;

import com.qqzeng.bloom.common.bean.BloomRequest;
import com.qqzeng.bloom.common.bean.BloomResponse;
import com.qqzeng.bloom.client.BloomClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * RPC proxy.
 * </p>
 * Created by qqzeng.
 */
public class BloomProxy implements InvocationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BloomProxy.class);

    private Class<?> clazz;
    private String serviceVersion = "";
    private BloomClient bloomClient;

    public BloomProxy(BloomClient bloomClient) {
        this.bloomClient = bloomClient;
    }

    public void setServiceVersion(String serviceVersion) {
        if (serviceVersion != null){
            this.serviceVersion = serviceVersion;
        } else {
            LOGGER.warn("Error Service version.");
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T newProxy(Class<?> clazz, String serviceVersion) {
        if (serviceVersion == null) {
            LOGGER.warn("Error Service version.");
            serviceVersion = "";
        }
        this.serviceVersion = serviceVersion;
        this.clazz = clazz;
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{this.clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        /**
         * Build RPC request.
         */
        BloomRequest bloomRequest = new BloomRequest();
        bloomRequest.setRequestId(UUID.randomUUID().toString());
        bloomRequest.setInterfaceName(this.clazz.getName());
        bloomRequest.setMethodName(method.getName());
        bloomRequest.setServiceVersion(this.serviceVersion);
        bloomRequest.setParameters(args);
        bloomRequest.setParameterTypes(method.getParameterTypes());
        /**
         * Call {@link BloomClient} to send request.
         */
        BloomResponse bloomResponse = new BloomResponse();
        final CountDownLatch completedSignal = new CountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    bloomClient.requestSend(bloomRequest, bloomResponse, completedSignal);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        /* Wait until server sends response, and client filled response.*/
        completedSignal.await();
        return bloomResponse.getResult();
    }
}
