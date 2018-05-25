package com.qqzeng.bloom.demo.client;

import com.qqzeng.bloom.client.BloomClient;
import com.qqzeng.bloom.client.proxy.BloomProxy;
import com.qqzeng.bloom.demo.server.api.BusService;
import com.qqzeng.bloom.demo.server.api.PersonService;
import com.qqzeng.bloom.demo.server.bean.Bus;
import com.qqzeng.bloom.demo.server.bean.Person;
import com.qqzeng.bloom.registry.zk.ZkServiceDiscovery;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author qqzeng
 * @desc
 */
public class DemoClient {

    static ApplicationContext context = new ClassPathXmlApplicationContext("bloom-demo-client-spring.xml");
    static BloomProxy bloomProxy = DemoClient.context.getBean(BloomProxy.class);

    public static void main(String[] args) throws InterruptedException {
       testPerson();
//        testBus();
//        testPerson2();
//        testPerson3();
    }

    /**
     * Unpack test case.
     */
    private static void testPerson2() throws InterruptedException {
        PersonService personService = DemoClient.bloomProxy.newProxy(PersonService.class, "");
        String name = "This is the name of person ";
        String str = "";
        for (int i = 0; i < 200; i++) {
            str += name + i + ". ";
        }
        String result = personService.getPersonByName(str);
        System.out.println(result);

//        Thread.currentThread().sleep(1000 * 60 );
    }

    private static void testPerson3() throws InterruptedException {
        ZkServiceDiscovery zkServiceDiscovery = new ZkServiceDiscovery("192.168.0.106:2181");
        BloomClient bloomClient = new BloomClient(zkServiceDiscovery);
        BloomProxy bloomProxy = new BloomProxy(bloomClient);
        PersonService personService = bloomProxy.newProxy(PersonService.class, "");
        String name = "This is the name of person.";
        String str = "";
        for (int i = 0; i < 200; i++) {
            str += name + i + " ";
        }
        String result = personService.getPersonByName(str);
        System.out.println(result);

//        Thread.currentThread().sleep(1000 * 60 );
    }

    private static void testBus() {
        BusService busService = DemoClient.bloomProxy.newProxy(BusService.class, "");
        Person person1 = new Person("杰伦", "周", 38);
        Person person2 = new Person("董", "周", 38);
        Person person3 = new Person("Jay", "Chou", 38);
        Bus bus = new Bus(Arrays.asList(person1, person2, person3), "001", "Benz", new Date());
        int number = busService.getPassagerNumber(bus);
        System.out.println(number);
    }

    private static void testPerson() {
        PersonService personService = DemoClient.bloomProxy.newProxy(PersonService.class, "");

        Person person = new Person("杰伦", "周", 38);
        personService.sayHi(person);
        String result = personService.getPersonInfo(person);
        System.out.println(result);

        PersonService personService2 = bloomProxy.newProxy(PersonService.class, "demo.personService2");
        Person person2 = new Person("董", "周", 38);
        String result2 = personService2.getPersonInfo(person2);
        System.out.println(result2);

    }

}



