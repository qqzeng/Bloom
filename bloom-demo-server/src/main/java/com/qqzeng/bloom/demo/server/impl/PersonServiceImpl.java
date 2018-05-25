package com.qqzeng.bloom.demo.server.impl;

import com.qqzeng.bloom.demo.server.api.PersonService;
import com.qqzeng.bloom.demo.server.bean.Person;
import com.qqzeng.bloom.server.annotations.BloomService;
import org.springframework.stereotype.Component;

/**
 * @author qqzeng
 * @desc
 */
@BloomService(value = PersonService.class)
public class PersonServiceImpl implements PersonService {
    @Override
    public void sayHi(Person p) {
        System.out.println("Hi " + p.getFirstName() + '!');
    }

    @Override
    public String getPersonInfo(Person p) {
        return "Person Info [" + "Name: " + p.getFirstName() + " " + p.getLastName() +
                ", Age: " + p.getAge() + "]";
    }

    @Override
    public String getPersonByName(String name) {
        return "[Person Name: " + name + "]";
    }
}
