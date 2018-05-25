package com.qqzeng.bloom.serializer.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.qqzeng.bloom.serializer.Serializer;

import java.io.IOException;

/**
 * Serializer implementation based on FastJson.
 * </p>
 * Created by qqzeng.
 */
public class FastJsonSerializer implements Serializer {

    @Override
    public byte[] serialize(Object obj) throws IOException {
        /**
         * <code>SerializerFeature.SortField</code>
         * High serialization performance will be obtained in the case of
         * sorting by fields at both serialization and deserialization stage.
         *
         * <code>SerializerFeature.DisableCircularReferenceDetect</code>
         * Avoid using object reference when occurring repeated references.
         */
        byte[] bytes = JSON.toJSONBytes(obj, SerializerFeature.SortField, SerializerFeature.DisableCircularReferenceDetect);
        return bytes;
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException {
        T result = JSON.parseObject(bytes, clazz, Feature.SortFeidFastMatch);
        return result;
    }

    /**
     * Covert one type of instance to another type.
     */
    public static Object convertObjectType(Class<?> type, Object obj) {
        Object res = null;
        if (obj != null) {
            if (!type.isInstance(obj)) {
                res = JSONObject.parseObject(JSON.toJSONBytes(obj), type);
            }
        }
        return res == null ? obj : res;
    }
}
