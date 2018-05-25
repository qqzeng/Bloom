package com.qqzeng.bloom.serializer.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qqzeng.bloom.serializer.Serializer;
import com.qqzeng.bloom.serializer.SerializerLoader;
import com.qqzeng.bloom.serializer.fastjson.FastJsonSerializer;
import com.qqzeng.bloom.serializer.protostuff.ProtoStuffSerializer;
import org.junit.Test;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author qqzeng
 * @desc Serializer api test cases.
 */
public class SerializerTest {

    @Test
    public void serializerTest() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());
        Student s1 = new Student("201712345678", "zs", 23, Arrays.asList("Beijing", " Shanghai", "WuHan", "Hangzhou"));
        byte[] bytes = objectMapper.writeValueAsBytes(s1);
        Student s2 = objectMapper.readValue(bytes, Student.class);
        System.out.println(s2.getName());
        System.out.println(s2.getAddresses());
    }

    @Test
    public void protostuffSerializerCachedTest() throws IOException {
        ProtoStuffSerializer ps = new ProtoStuffSerializer();
        Student s1 = new Student("201712345678", "zs", 23, Arrays.asList("Beijing", " Shanghai", "WuHan", "Hangzhou"));
        byte[] bytes = ps.serialize(s1);
        Student s2 =ps.deserialize(bytes, Student.class);
        System.out.println(s2.getName());
        System.out.println(s2.getAddresses());
    }

    @Test
    public void testLoadSerializer() throws Exception {
        Serializer ser = SerializerLoader.load();
        Student s1 = new Student("201712345678", "zs", 23, Arrays.asList("Beijing", " Shanghai", "WuHan", "Hangzhou"));
        byte[] bytes = ser.serialize(s1);
        Student s2 =ser.deserialize(bytes, Student.class);
        System.out.println(s2.getName());
        System.out.println(s2.getAddresses());
    }

    @Test
    public void testTypeConverter() throws Exception {
        Student s1 = new Student("201712345678", "zs", 23, Arrays.asList("Beijing", " Shanghai", "WuHan", "Hangzhou"));
        Teacher t1 = new Teacher(001, "t1");
        Teacher t2 = new Teacher(002, "t2");
        Teacher t3 = new Teacher(003, "t3");
        Teacher t4 = new Teacher(004, "t4");
        HashMap<String, List<Object>> stringListHashMap = new HashMap<>();
        stringListHashMap.put(s1.getName(), Arrays.asList(t1, t2, t3, t4));
        CompositeObj compositeObj = new CompositeObj(s1, stringListHashMap);
        byte[] bytes = JSON.toJSONBytes(compositeObj, SerializerFeature.SortField);
        Object result = JSON.parseObject(bytes, compositeObj.getClass(), Feature.SortFeidFastMatch);
        Object obj = FastJsonSerializer.convertObjectType(compositeObj.getClass(), result);
        System.out.println(obj);
    }

}
