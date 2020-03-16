package com.example.zhaojing5.myapplication.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * created by zhaojing 2020/3/12 下午2:19
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventType {

    Class listenerType();

    String listenerSetter();

    String methodName();
}
