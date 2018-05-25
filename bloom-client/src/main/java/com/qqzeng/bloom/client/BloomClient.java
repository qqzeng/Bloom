package com.qqzeng.bloom.client;

import com.qqzeng.bloom.codecb.BloomDecoder;
import com.qqzeng.bloom.codecb.BloomEncoder;
import com.qqzeng.bloom.common.bean.BloomRequest;
import com.qqzeng.bloom.common.bean.BloomResponse;
import com.qqzeng.bloom.registry.ServiceDiscover;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by qqzeng.
 * </p>
 * RPC client.
 */
// TODO: singleton or prototype.
public class BloomClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(BloomClient.class);

    private final ServiceDiscover serviceDiscover;

    private final HeartBeatClientHandler heartBeatClientHandler = new HeartBeatClientHandler();

    private Bootstrap bootstrap;

    private EventLoopGroup group;

    private ConnectionWatchHandler connectionWatchHandler;

    private String host;
    private int port;

    public BloomClient(ServiceDiscover serviceDiscover) {
        this.serviceDiscover = serviceDiscover;

    }

    private void resolveConnectionInfo(BloomRequest bloomRequest) {
        /**
         * Get service address.
         */
        String interfaceName = bloomRequest.getInterfaceName();
        String serviceVersion = bloomRequest.getServiceVersion();
        if (null == serviceDiscover) {
            LOGGER.debug("Service Discover instantiation failure.");
            return;
        }
        String serviceName = interfaceName;
        if (!serviceVersion.isEmpty()) {
            serviceName = interfaceName + "-" + serviceVersion;
        }
        String serviceAddress = serviceDiscover.discover(serviceName);
        LOGGER.debug("Client discover service: {} from {}", serviceName, serviceAddress);
        /**
         * Resolve server.
         */
        String[] splits = serviceAddress.split(":");
        String host = splits[0];
        int port = Integer.parseInt(splits[1]);
        this.host = host;
        this.port = port;
    }

    private void initConnection(BloomRequest bloomRequest, BloomResponse bloomResponse,
                                CountDownLatch  completedSignal) throws Exception {
        this.group = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        this.connectionWatchHandler = new ConnectionWatchHandler(this, true) {
            @Override
            public ChannelHandler[] handlers() {
                return new ChannelHandler[]{
                        /* Reconnection handler. */
                        this,
                        /* Heartbeat Watcher Handler. */
                        new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS),
                        /* Heartbeat respond Handler. */
                        heartBeatClientHandler,
                        new LengthFieldBasedFrameDecoder(
                                Integer.MAX_VALUE, 0, 4, 0, 4),
                        new LengthFieldPrepender(4),
                        new BloomEncoder(BloomRequest.class),
                        new BloomDecoder(BloomResponse.class),
                        /* Client Business Handler. */
                        new BloomDefaultClientHandler(bloomRequest, bloomResponse, completedSignal)
                };
            }

            @Override
            public int size() {
                return handlers().length;
            }
        };
        this.bootstrap.group(this.group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true);
//                .handler(new LoggingHandler(LogLevel.INFO));
    }

    public void connect() throws Exception {
        final ChannelFuture future;
        try {
            synchronized (bootstrap) {
                bootstrap.handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(BloomClient.this.connectionWatchHandler.handlers());
                    }
                });
                // TODO: Reused the channel/connection.
                future = bootstrap.connect(this.host, this.port).addListener(
                        BloomClient.this.connectionWatchHandler.getMyChannelFutureListener());
            }
            future.sync();
//            future.channel().closeFuture().sync();
        } catch (Throwable t) {
//            throw new Exception("connects to  fails", t);
            LOGGER.debug("Initialization connection fails (cause: " + t + " ),  ready to reconnect.");
        } finally {
//            group.shutdownGracefully();
        }
    }


    public synchronized void requestSend(BloomRequest bloomRequest, BloomResponse bloomResponse, CountDownLatch  completedSignal) throws Exception {
        /* RPC response */
        initConnection(bloomRequest, bloomResponse, completedSignal);
        resolveConnectionInfo(bloomRequest);
        connect();
    }

}
