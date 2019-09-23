package com.example.javalib.proxy;

public class ProxyObject extends AbstractObject {
    private RealObject realObject = new RealObject();

    @Override
    public void operation() {
        System.out.println("before --->proxy object do some operations.");
        realObject.operation();
        System.out.println("after --->proxy object do some operations.");
    }

    //use proxy mode
    /*public void testProxy(){
        AbstractObject proxyObject = new ProxyObject();
        proxyObject.operation();
    }*/
}
