package com.qqzeng.bloom.serializer;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Load and return Serializer instance.
 * </p>
 * Created by qqzeng.
 */
public class SerializerLoader {
    public static Serializer load() {
        final Iterator<Serializer> serializerIterator = ServiceLoader.load(Serializer.class).iterator();
        if (serializerIterator.hasNext()) {
            return serializerIterator.next();
        }
        return null;
    }
}
