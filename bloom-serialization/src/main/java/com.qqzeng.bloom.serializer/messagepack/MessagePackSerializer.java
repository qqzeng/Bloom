package com.qqzeng.bloom.serializer.messagepack;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qqzeng.bloom.serializer.Serializer;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.io.IOException;

/**
 * Serializer implementation based on MessagePack.
 *     More: https://github.com/komamitsu/jackson-dataformat-msgpack
 *           https://github.com/msgpack/msgpack-java/tree/v07-develop/msgpack-jackson
 * </p>
 * Created by qqzeng.
 */
public class MessagePackSerializer implements Serializer {

    private final ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());

    @Override
    public byte[] serialize(Object obj) throws IOException {
        byte[] bytes = objectMapper.writeValueAsBytes(obj);
        return bytes;
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException{
        T result = objectMapper.readValue(bytes, clazz);
        return result;
    }
}
