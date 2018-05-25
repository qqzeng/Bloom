package com.qqzeng.bloom.client;

import io.netty.channel.ChannelHandler;

/**
 * ChannelHandler Holder to save all {@link ChannelHandler}s.
 * <p/>
 * Created by qqzeng.
 */
public interface ChannelHandlerHolder {
    ChannelHandler[] handlers();
    int size();
}
