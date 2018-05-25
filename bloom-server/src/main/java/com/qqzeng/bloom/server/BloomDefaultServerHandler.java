package com.qqzeng.bloom.server;

import com.qqzeng.bloom.common.bean.BloomRequest;
import com.qqzeng.bloom.common.bean.BloomResponse;
import com.qqzeng.bloom.serializer.fastjson.FastJsonSerializer;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

/**
 * BloomServer Handler.
 * </p>
 * Created by qqzeng.
 */
public class BloomDefaultServerHandler extends SimpleChannelInboundHandler<BloomRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BloomDefaultServerHandler.class);

    private Map<String, Object> handlerMap;

    public BloomDefaultServerHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    public void setHandlerMap(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    public void channelRead0(ChannelHandlerContext channelHandlerContext,
                             BloomRequest bloomRequest) throws Exception {
        BloomResponse bloomResponse = new BloomResponse();
        bloomResponse.setRequestId(bloomRequest.getRequestId());
        try {
            Object result = handleRequest(bloomRequest);
            bloomResponse.setResult(result);
        } catch (Exception e) {
            LOGGER.error("Handle result failure", e);
            bloomResponse.setException(e);
        }
//        channelHandlerContext.writeAndFlush(bloomResponse).addListener(ChannelFutureListener.CLOSE);
        channelHandlerContext.writeAndFlush(bloomResponse);
        LOGGER.debug("Server response at " + new Date());
    }

    private Object handleRequest(BloomRequest bloomRequest) {
        Object result = null;

        /* Get service bean. */
        String interfaceName = bloomRequest.getInterfaceName();
        String serviceName = interfaceName;
        String serviceVersion = bloomRequest.getServiceVersion();
        if (!serviceVersion.isEmpty()) {
            serviceName += "-" + serviceVersion;
        }
        Object serviceBean = handlerMap.get(serviceName);
        if (null == serviceBean) {
            LOGGER.error("Service bean hasn't been found!");
            /**
             * TODO: result can't be null.
             */
            return result;
        }
        LOGGER.debug("Service bean has been found at " + new Date());

        /* Reflect to invoke. */
        Class<?> beanClazz = serviceBean.getClass();
        String methodName = bloomRequest.getMethodName();
        Class<?>[] parameterTypes = bloomRequest.getParameterTypes();
        Object[] parameters = bloomRequest.getParameters();
        Object[] params = convertParameterType(parameterTypes, parameters);
        try {
            Method[] methods = beanClazz.getDeclaredMethods();
            for (Method m : methods) {
                if (!m.isAccessible()) {
                    m.setAccessible(true);
                }
                if (m.getName().equals(methodName)) {
                    result = m.invoke(serviceBean, params);
                    break;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }


    private Object[] convertParameterType(Class<?>[] parameterTypes, Object[] parameters) {
        if (parameters != null) {
            boolean isConverted = false;
            Object[] res = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                if (!parameterTypes[i].isInstance(parameters[i])) {
                    res[i] = FastJsonSerializer.convertObjectType(parameterTypes[i], parameters[i]);
                    isConverted = true;
                }
            }
            return isConverted ? res : parameters;
        }
        return null;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        LOGGER.error(cause.getMessage());
        ctx.close();
    }

}
