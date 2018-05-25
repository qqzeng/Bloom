package com.qqzeng.bloom.server;


import com.qqzeng.bloom.codecb.BloomDecoder;
import com.qqzeng.bloom.codecb.BloomEncoder;
import com.qqzeng.bloom.common.bean.BloomRequest;
import com.qqzeng.bloom.common.bean.BloomResponse;
import com.qqzeng.bloom.registry.ServiceRegistry;
import com.qqzeng.bloom.server.annotations.BloomService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Rpc Server.
 * </p>
 * Created by qqzeng.
 */
public class BloomServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(BloomServer.class);

    private String serviceAddress;
    private ServiceRegistry serviceRegistry;

    private Map<String, Object> handlerMap = new HashMap<>();

    private final HeartBeatServerHandler heartBeatServerHandler = new HeartBeatServerHandler();

    public BloomServer(String serviceAddress, ServiceRegistry serviceRegistry) {
        this.serviceAddress = serviceAddress;
        this.serviceRegistry = serviceRegistry;
    }

    /**
     * Invoked by Spring after its instantiation.
     * @throws Exception
     */
    public void serverSetup() throws Exception {

        registerServiceInfo();

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // Heartbeat trigger handler.
                            socketChannel.pipeline().addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
                            // Heartbeat respond handler.
                            socketChannel.pipeline().addLast(heartBeatServerHandler);
                            // Unpack and (stick) pack handler.
                            socketChannel.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(
                                    Integer.MAX_VALUE, 0, 4, 0, 4));
                            socketChannel.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));
                            socketChannel.pipeline().addLast(new BloomDecoder(BloomRequest.class));
                            socketChannel.pipeline().addLast(new BloomEncoder(BloomResponse.class));
//                            socketChannel.pipeline().addLast(new BloomServerHandler(handlerMap));
                            // Default business handler.
                            socketChannel.pipeline().addLast(new BloomDefaultServerHandler(handlerMap));
                        }
                    });
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            String[] splits = serviceAddress.split(":");
            String ip = splits[0];
            int port = Integer.parseInt(splits[1]);
            ChannelFuture future = serverBootstrap.bind(ip, port).sync();

            /**
             * Register services.
             */
            if (serviceRegistry != null) {
                for (String interfaceName : handlerMap.keySet()) {
                    serviceRegistry.register(interfaceName, serviceAddress);
                    LOGGER.debug("Register service: {} on {}", interfaceName, serviceAddress);
                }
            }

            LOGGER.debug("Server started on port {}", port);
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private void registerServiceInfo() {
        Map<String, Object> beansMap = SpringContextUtils.getBeansWithAnnotation(BloomService.class);
        if (!beansMap.isEmpty()) {
            for (Map.Entry<String, Object> entry : beansMap.entrySet()) {
                BloomService bloomService = entry.getValue().getClass().getAnnotation(BloomService.class);
                String serviceName = bloomService.value().getName();
                String serviceVersion = bloomService.version();
                if (!serviceVersion.isEmpty()) {
                    serviceName += "-" + serviceVersion;
                }
                this.handlerMap.put(serviceName, entry.getValue());
            }
        }
    }
}
