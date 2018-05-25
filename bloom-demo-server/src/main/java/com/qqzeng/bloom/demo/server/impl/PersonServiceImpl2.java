package com.qqzeng.bloom.demo.server.impl;

import com.qqzeng.bloom.demo.server.api.PersonService;
import com.qqzeng.bloom.demo.server.bean.Person;
import com.qqzeng.bloom.server.annotations.BloomService;

/**
 * @author qqzeng
 * @desc
 */
@BloomService(value = PersonService.class, version = "demo.personService2")
public class PersonServiceImpl2 implements PersonService {
    @Override
    public void sayHi(Person p) {
        System.out.println("Hi " + p.getFirstName() + '!');
    }

    @Override
    public String getPersonInfo(Person p) {
        return "[demo.helloService2] Person Info [" + "Name: " + p.getLastName() + " " + p.getFirstName() +
                ", Age: " + p.getAge() + "]";
    }

    @Override
    public String getPersonByName(String name) {
        return "[demo.helloService2] [Person Name: " + name + "]";
    }
}
