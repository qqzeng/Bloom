package com.qqzeng.bloom.serializer.protostuff;

import io.protostuff.LinkedBuffer;

import java.lang.ref.SoftReference;

/**
 * Reused <code>LinkedBuffer</code> with <code>ThreadLocal</code> to avoid excessive memory allocation and improve performance.
 * <p>
 * About SoftReference: https://www.cnblogs.com/dolphin0520/p/3784171.html
 *      source code: @see {@link java.lang.ref.SoftReference}
 * </p>
 * <p>
 * About ThreadLocal: https://blog.csdn.net/hxpjava1/article/details/58591947
 *                    https://github.com/google/guava/pull/2112
 *                    https://droidyue.com/blog/2016/03/13/learning-threadlocal-in-java/
 *      source code: see {@link java.lang.ThreadLocal}
 * </p>
 * Created by qqzeng.
 */
public class ThreadLocalLinkedBufferAllocator {
    private static ThreadLocal<SoftReference<LinkedBuffer>> tlba = new ThreadLocal<>();

    public ThreadLocalLinkedBufferAllocator() {
    }

    public static LinkedBuffer getBufferAllocator() {
        SoftReference<LinkedBuffer> bAllocatorRef = tlba.get();
        if (bAllocatorRef == null || bAllocatorRef.get() == null) {
            bAllocatorRef = new SoftReference<>(LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
            tlba.set(bAllocatorRef);
        }

        return bAllocatorRef.get();
    }
}
