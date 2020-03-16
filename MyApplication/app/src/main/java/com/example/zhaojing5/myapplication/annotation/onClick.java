package com.example.zhaojing5.myapplication.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * created by zhaojing 2020/3/12 下午2:21
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventType(listenerType = View.OnClickListener.class, listenerSetter = "setOnClickListener", methodName = "onClick")
public @interface onClick {

    int[] value();

}
