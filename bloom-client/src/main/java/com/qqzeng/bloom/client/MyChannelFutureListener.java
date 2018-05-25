package com.qqzeng.bloom.client;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by qqzeng.
 */
public class MyChannelFutureListener implements ChannelFutureListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyChannelFutureListener.class);

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        boolean success = future.isSuccess();
        if (!success) {
            LOGGER.debug("[ConnectionWatchHandler] Reconnection fail at " + new Date() + ".");
            /* Trigger the reconnection operation again. */
            future.channel().pipeline().fireChannelInactive();
        } else {
            LOGGER.debug("[ConnectionWatchHandler] Reconnection success at " + new Date() + ".");
        }
    }
}
