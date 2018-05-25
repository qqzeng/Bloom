package com.qqzeng.bloom.client;

import com.qqzeng.bloom.common.bean.BloomRequest;
import com.qqzeng.bloom.common.bean.BloomResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * BloomClient default handler.
 * </p>
 * Created by qqzeng.
 */
public class BloomDefaultClientHandler extends SimpleChannelInboundHandler<BloomResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BloomDefaultClientHandler.class);

    private BloomResponse bloomResponse;
    private CountDownLatch completedSignal;
    private BloomRequest bloomRequest;

    public BloomDefaultClientHandler() {

    }

    public BloomDefaultClientHandler(BloomRequest bloomRequest, BloomResponse bloomResponse, CountDownLatch completedSignal) {
        this.bloomResponse = bloomResponse;
        this.bloomRequest = bloomRequest;
        this.completedSignal = completedSignal;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BloomResponse response) throws Exception {
        this.bloomResponse.setRequestId(response.getRequestId());
        this.bloomResponse.setException(response.getException());
        this.bloomResponse.setResult(response.getResult());
        LOGGER.debug("Set response over!");
        if (this.bloomRequest != null) {
            completedSignal.countDown();
//            ReferenceCountUtil.release(response);
            this.bloomRequest = null;
            return;
        }
        LOGGER.debug("Abnormal server response information!");
        LOGGER.debug("Info: " + response.getResult().toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        LOGGER.error(cause.getMessage());
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[BloomDefaultClientHandler] Client channel inactive at "+ new Date());
        ctx.fireChannelInactive();
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.debug("[BloomDefaultClientHandler] Client channel active at "+ new Date());
        sendRequest(ctx.channel());
    }

    public boolean sendRequest(final Channel cf) {
        if (bloomRequest != null) {
            cf.writeAndFlush(bloomRequest);
            LOGGER.debug("[BloomDefaultClientHandler] Client send request over at " + new Date());
            return true;
        } else {
            LOGGER.warn("[BloomDefaultClientHandler] No package available to send.");
            return false;
        }
    }

    // Getters and Setters.
    public BloomResponse getBloomResponse() {
        return bloomResponse;
    }

    public void setBloomResponse(BloomResponse bloomResponse) {
        this.bloomResponse = bloomResponse;
    }

    public CountDownLatch getCompletedSignal() {
        return completedSignal;
    }

    public void setCompletedSignal(CountDownLatch completedSignal) {
        this.completedSignal = completedSignal;
    }

    public BloomRequest getBloomRequest() {
        return bloomRequest;
    }

    public void setBloomRequest(BloomRequest bloomRequest) {
        this.bloomRequest = bloomRequest;
    }


}
