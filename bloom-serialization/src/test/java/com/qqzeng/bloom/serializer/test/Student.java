package com.qqzeng.bloom.serializer.test;

import java.util.List;

/**
 * @author qqzeng
 * @desc
 */
public class Student {
    private String id;
    private String name;
    private Integer age;
    private List<String> addresses;

    public Student(){

    }
    public Student(String id, String name, Integer age, List<String> addresses) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.addresses = addresses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }
}
