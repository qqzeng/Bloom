package com.qqzeng.bloom.codecb;

import com.qqzeng.bloom.serializer.Serializer;
import com.qqzeng.bloom.serializer.SerializerLoader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * RPC messages/packages decoder based on netty.
 * </p>
 * Created by qqzeng.
 */
public class BloomDecoder extends ByteToMessageDecoder {

    private Class<?> clazz;

    public BloomDecoder(Class<?> clazz) {
        this.clazz = clazz;
    }

    /**
     *  Custom operation for pack and unpack.
     */

//    @Override
//    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
//        if (byteBuf.readableBytes() < Integer.BYTES) {
//            return;
//        }
//        byteBuf.markReaderIndex();
//        int len = byteBuf.readInt();
//        if (byteBuf.readableBytes() < len) {
//            byteBuf.resetReaderIndex();
////            throw new RuntimeException("Illegal buffer: insufficient buffer length.");
//            return;
//        }
//        final Serializer serializer = SerializerLoader.load();
//        final byte[] bytes = new byte[byteBuf.readableBytes()];
//        byteBuf.readBytes(bytes);
//        final Object obj = serializer.deserialize(bytes, clazz);
//        list.add(obj);
//    }

    /**
     * Use <code>LengthFieldBasedFrameDecoder</code> and <code>LengthFieldPrepender</code> to handle pack and unpack.
     * see {@link io.netty.handler.codec.LengthFieldBasedFrameDecoder} and {@link io.netty.handler.codec.LengthFieldPrepender}
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        final Serializer serializer = SerializerLoader.load();
        final byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        final Object obj = serializer.deserialize(bytes, clazz);
        list.add(obj);
    }
}
