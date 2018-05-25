package com.qqzeng.bloom.serializer.protostuff;


import com.qqzeng.bloom.serializer.Serializer;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * Serializer implementation based on ProtoStuff.
 *      More: https://github.com/protostuff/protostuff
 * </p>
 * Created by qqzeng.
 */
public class ProtoStuffSerializer  implements Serializer{

    @SuppressWarnings("unchecked")
    public byte[] serialize(Object obj) {
//        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        /**
         * Use <code>ThreadLocal</code> to avoid thread conflicting condition when allocating memory frequently in heap.
         *
         * Get cached <code>LinkedBuffer</code> instance from thread local map if the corresponding soft reference has't been gc,
         * otherwise allocate a new one.
         */
        LinkedBuffer buffer = ThreadLocalLinkedBufferAllocator.getBufferAllocator();
        try {
            Schema schema = RuntimeSchema.getSchema(obj.getClass());
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        T obj = null;
        try {
            obj = clazz.newInstance();
            Schema schema = RuntimeSchema.getSchema(obj.getClass());
            ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
