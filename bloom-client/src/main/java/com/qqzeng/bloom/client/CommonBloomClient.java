package com.qqzeng.bloom.client;

import com.qqzeng.bloom.codecb.BloomDecoder;
import com.qqzeng.bloom.codecb.BloomEncoder;
import com.qqzeng.bloom.common.bean.BloomRequest;
import com.qqzeng.bloom.common.bean.BloomResponse;
import com.qqzeng.bloom.registry.ServiceDiscover;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by qqzeng.
 * </p>
 * RPC client.
 */
public class CommonBloomClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonBloomClient.class);

    private ServiceDiscover serviceDiscover;

    public CommonBloomClient(ServiceDiscover serviceDiscover) {
        this.serviceDiscover = serviceDiscover;
    }

    public void setServiceDiscover(ServiceDiscover serviceDiscover) {
        this.serviceDiscover = serviceDiscover;
    }

    public BloomResponse requestSend(BloomRequest bloomRequest, BloomResponse bloomResponse, CountDownLatch  completedSignal) throws Exception {
        /* RPC response */
//        BloomResponse bloomResponse = new BloomResponse();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast("ping", new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS));
                            socketChannel.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(
                                    Integer.MAX_VALUE, 0, 4, 0, 4));
                            socketChannel.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));
                            socketChannel.pipeline().addLast(new BloomEncoder(BloomRequest.class));
                            socketChannel.pipeline().addLast(new BloomDecoder(BloomResponse.class));
//                            socketChannel.pipeline().addLast(new BloomClientHandler(bloomResponse, bloomRequest, completedSignal));
                            socketChannel.pipeline().addLast(new BloomDefaultClientHandler(bloomRequest, bloomResponse, completedSignal));
                        }
                    });
            bootstrap.option(ChannelOption.TCP_NODELAY, true);

            /**
             * Get service address.
             */
            String interfaceName = bloomRequest.getInterfaceName();
            String serviceVersion = bloomRequest.getServiceVersion();
            if (null == serviceDiscover) {
                LOGGER.debug("Service Discover instantiation failure.");
                return bloomResponse;
            }
            String serviceName = interfaceName;
            if (!serviceVersion.isEmpty()) {
                serviceName = interfaceName + "-" + serviceVersion;
            }
            String serviceAddress = serviceDiscover.discover(serviceName);
            LOGGER.debug("Client discover service: {} from {}", serviceName, serviceAddress);

            /**
             * Connect to server.
             */
            String[] splits = serviceAddress.split(":");
            String ip = splits[0];
            int port = Integer.parseInt(splits[1]);
            ChannelFuture future = bootstrap.connect(ip, port).sync();

            /**
             * Send request.
             */
            Channel channel = future.channel();
//            channel.writeAndFlush(bloomRequest).sync();
            ChannelFuture future1 = channel.closeFuture();
            future1.sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
        return bloomResponse;
    }
}
