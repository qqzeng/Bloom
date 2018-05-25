package com.qqzeng.bloom.demo.server.api;

import com.qqzeng.bloom.demo.server.bean.Person;

/**
 * @author qqzeng
 * @desc Server exported API.
 */
public interface PersonService {

    void sayHi(Person p);

    String getPersonInfo(Person p);

    String getPersonByName(String name);
}
