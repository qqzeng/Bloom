package com.qqzeng.bloom.client;

import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by qqzeng.
 */
public class ReConnectionTimerTask implements TimerTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReConnectionTimerTask.class);

    private final BloomClient bc;

    public ReConnectionTimerTask(BloomClient bc) {
        this.bc = bc;
    }

    /**
     * Timer task for reconnection.
     */
    @Override
    public void run(Timeout timeout) throws Exception {
        bc.connect();
    }
}
