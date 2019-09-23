package com.example.zhaojing5.myapplication.proxy;

public class RealObject extends AbstractObject {
    @Override
    public void operation() {
        System.out.println("do something real.");
    }
}
