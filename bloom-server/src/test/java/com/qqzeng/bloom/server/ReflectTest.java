package com.qqzeng.bloom.server;

import org.junit.Test;

/**
 * @author qqzeng
 * @desc
 */
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectTest {
    public static void main(String[] args) {
        try {
            Class catClass = Class.forName("com.qqzeng.bloom.server.CatService");
            Object obj = catClass.newInstance();
            Method[] methods = catClass.getMethods();
            Object o = new Cat("Tom");
            for(Method method : methods) {
                if("getCatInfo".equals(method.getName())) {
                   Object result = method.invoke(obj,  o);
                    System.out.println(result);
                }
            }
//            catClass.getMethod("getCatInfo", Cat.class).invoke(obj, null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } /*catch (NoSuchMethodException e) {
            e.printStackTrace();
        }*/
    }
}

class Cat {
    private String name;

    public String getName() {
        return name;
    }
    public Cat(String name) {
        this.name = name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void shout() {
        System.out.println("My name is " + this.name + "!");
    }
}

class CatService{
    public String getCatInfo(Cat c) {
        if (c != null) {
            return "[Name : " + c.getName() + "]";
        }
        return "No Cat Info.";
    }
}