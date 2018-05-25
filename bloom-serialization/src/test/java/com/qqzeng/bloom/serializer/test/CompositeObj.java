package com.qqzeng.bloom.serializer.test;

import java.util.List;
import java.util.Map;

/**
 * @author qqzeng
 * @desc
 */
public class CompositeObj {
    private Student stu;
    private Map<String, List<Object>> teacherMap;

    public void setStu(Student stu) {
        this.stu = stu;
    }

    public Student getStu() {
        return stu;
    }

    public void setTeacherMap(Map<String, List<Object>> teacherMap) {
        this.teacherMap = teacherMap;
    }

    public Map<String, List<Object>> getTeacherMap() {
        return teacherMap;
    }

    public CompositeObj(Student stu, Map<String, List<Object>> teacherMap) {
        this.stu = stu;
        this.teacherMap = teacherMap;
    }

    public CompositeObj() {
    }

    @Override
    public String toString() {
        return "CompositeObj{" +
                "stu=" + stu +
                ", teacherMap=" + teacherMap +
                '}';
    }
}
