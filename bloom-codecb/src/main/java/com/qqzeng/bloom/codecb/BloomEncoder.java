package com.qqzeng.bloom.codecb;

import com.qqzeng.bloom.common.bean.BloomRequest;
import com.qqzeng.bloom.serializer.Serializer;
import com.qqzeng.bloom.serializer.SerializerLoader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * RPC messages/packages encoder based on netty.
 * </p>
 * Created by qqzeng.
 */
public class BloomEncoder extends MessageToByteEncoder {

    private Class<?> clazz;

    public BloomEncoder(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (this.clazz.isInstance(msg)) {
            final Serializer serializer = SerializerLoader.load();
            final byte[] bytes = serializer.serialize(msg);
            /* Append this line if handling pack and unpack ourselves. */
            /* out.writeInt(bytes.length) ;*/
            out.writeBytes(bytes);
        }
    }
}
