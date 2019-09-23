package com.example.javalib;

import com.example.javalib.proxy.AbstractObject;
import com.example.javalib.proxy.ProxyObject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MyClass {
    public static void main(String[] args){
        test();
        ITest iTest = (ITest) Proxy.newProxyInstance(ITest.class.getClassLoader(), new Class<?>[]{ITest.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                int value1 = (Integer) args[0];
                int value2 = (Integer) args[1];
//                SuppressWarnings suppressWarnings = method.getAnnotation(SuppressWarnings.class);//nullpointer ??
                System.out.println("参数为：" + value1 + " " + value2 + " " + "\n"
                        + "方法名为：" + method.getName() + " \n");
//                System.out.println("方法注释为：" + suppressWarnings.value() != null ? suppressWarnings.value().toString() : "is null");
                return null;
            }
        });
        iTest.testMethod(10,12);
    }
    public interface ITest{
        @SuppressWarnings({"warning1","warning2"})
        String testMethod(int a,int b);
    }

    public void testProxy(){
        AbstractObject proxyObject = new ProxyObject();
        proxyObject.operation();
    }

    public static void test(){
        String tmp = "        1757572 kB";
        String result = tmp.replaceAll("[\\D]","");
        System.out.println(result);
    }
}

