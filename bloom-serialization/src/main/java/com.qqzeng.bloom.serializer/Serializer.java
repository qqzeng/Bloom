package com.qqzeng.bloom.serializer;

import java.io.IOException;

/**
 * Serializer interface.
 * </p>
 * Created by qqzeng.
 */
public interface Serializer {

    byte[] serialize(Object obj) throws IOException;

    <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException;
}
