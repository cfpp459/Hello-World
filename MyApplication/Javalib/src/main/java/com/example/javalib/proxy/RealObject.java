package com.example.javalib.proxy;

public class RealObject extends AbstractObject {
    @Override
    public void operation() {
        System.out.println("do something real.");
    }
}
