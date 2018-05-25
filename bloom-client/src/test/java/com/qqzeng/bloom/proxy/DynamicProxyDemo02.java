package com.qqzeng.bloom.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author qqzeng
 * @desc 动态代理测试demo
 */
class RPCProxyClient implements InvocationHandler {

    private Object obj;

    public RPCProxyClient(Object obj) {
        this.obj = obj;
    }

    /**
     * 得到被代理对象
     */
    public static Object getProxy(Object obj) {
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(),
                obj.getClass().getInterfaces(), new RPCProxyClient(obj));
    }

    /**
     * 调用此方法执行
     */
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        //结果参数;
        Object result = new Object();
        System.out.println("====before====");
        result = method.invoke(obj, args);
        System.out.println("====after====");
        return result;
    }
}

interface HelloWorldService {
    String sayHello(String msg);
}

class HelloWorldServiceImpl implements HelloWorldService {
    @Override
    public String sayHello(String msg) {
        String result = "hello world " + msg;
        System.out.println(result);
        return result;
    }
}

public class DynamicProxyDemo02 {
    public static void main(String[] args) {
        final HelloWorldServiceImpl hws = new HelloWorldServiceImpl();
        HelloWorldService helloWorldService = (HelloWorldService) RPCProxyClient.getProxy(hws);
        helloWorldService.sayHello("test");
    }
}