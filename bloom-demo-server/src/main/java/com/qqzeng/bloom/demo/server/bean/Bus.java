package com.qqzeng.bloom.demo.server.bean;

import java.util.Date;
import java.util.List;

/**
 * @author qqzeng
 * @desc
 */
public class Bus {
    private List<Person> personList;

    private String id;
    private String name;
    private Date productDate;

    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
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

    public Date getProductDate() {
        return productDate;
    }

    public void setProductDate(Date productDate) {
        this.productDate = productDate;
    }

    public Bus(List<Person> personList, String id, String name, Date productDate) {
        this.personList = personList;
        this.id = id;
        this.name = name;
        this.productDate = productDate;
    }

    public Bus() {

    }
}
