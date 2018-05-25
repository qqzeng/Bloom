package com.qqzeng.bloom.client;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.HashedWheelTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by qqzeng.
 * <p/>
 * Connection Watcher Handler to monitor the connection. Once the connection/channel interrupt/close,
 * it will reconnect to the server for <code>MAX_ATTEMPTS</code> times.
 * What's more it'll apply Exponential Backoff strategy to the process (same as the Channel Detection in computer network).
 */
@Sharable
public abstract class ConnectionWatchHandler
        extends ChannelInboundHandlerAdapter implements ChannelHandlerHolder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionWatchHandler.class);
    
    private final BloomClient bc;

    /* Timer to count the elapsed time.*/
    private final HashedWheelTimer timer = new HashedWheelTimer();

    /* Max number of attempts. */
    private final int MAX_ATTEMPTS = 16;

    /* Current number of attempts. */
    private volatile int attempts;

    /* Need to reconnect */
    private volatile boolean reconnect;

    private final ReConnectionTimerTask rctt;

    private final MyChannelFutureListener cfl;


    public MyChannelFutureListener getMyChannelFutureListener() {
        return this.cfl;
    }

    public ConnectionWatchHandler(BloomClient bc, boolean reconnect) {
        this.bc = bc;
        this.reconnect = reconnect;
        this.rctt = new ReConnectionTimerTask(this.bc);
        this.cfl = new MyChannelFutureListener();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.debug("[ConnectionWatchHandler] Channel has been inactive at " + new Date() + ".");
        if (reconnect) {
            LOGGER.debug("[ConnectionWatchHandler] Ready to reconnect for [" + attempts + "] times at " + new Date() + ".");
            if (attempts < MAX_ATTEMPTS) {
                attempts += 1;
                int timeout = 2 << attempts;
                timer.newTimeout(this.rctt, timeout, TimeUnit.MILLISECONDS);
            } else {
                /* Client close current channel. */
                LOGGER.debug("[ConnectionWatchHandler] Reconnection times exceed the maximum.");
                ctx.channel().close();
                ctx.close();
                LOGGER.debug("[ConnectionWatchHandler] Ready to exit!");
                System.exit(0);
            }
        }
        ctx.fireChannelInactive();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.debug("[ConnectionWatchHandler] Channel has been active at " + new Date() + ", and reset attempts = 0.");
        attempts = 0;
        ctx.fireChannelActive();
    }

}
