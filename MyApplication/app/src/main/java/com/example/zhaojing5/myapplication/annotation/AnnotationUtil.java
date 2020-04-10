package com.example.zhaojing5.myapplication.annotation;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * created by zhaojing 2020/3/12 下午2:44
 */
public class AnnotationUtil {
    public static final String TAG = AnnotationUtil.class.getSimpleName();

    public static void injectView(Activity activity){

        if (activity == null) {
            return;
        }

        Class<? extends Activity> activityClass = activity.getClass();
        Field[] declaredFileds = activityClass.getDeclaredFields();
        for (Field field : declaredFileds) {
            if(field.isAnnotationPresent(InjectView.class)){

                InjectView injectView = field.getAnnotation(InjectView.class);
                if (injectView != null) {
                   int value = injectView.value();

                    try {
                        Method method = activityClass.getMethod("findViewById", int.class);
                        method.setAccessible(true);
                        Object object = method.invoke(activity, value);
                        field.setAccessible(true);
                        field.set(activity, object);
                    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }

            }
        }

    }

    public static void injectEvent(Activity activity){

        if (activity == null) {
            return;
        }

        Class<? extends Activity> activityClass = activity.getClass();
        Method[] declaredMethods = activityClass.getDeclaredMethods();

        for (Method method : declaredMethods) {


            if(method.isAnnotationPresent(onClick.class)){
                Log.i(TAG, "method name is " + method.getName());
                onClick onClickAnnotation = method.getAnnotation(onClick.class);
                int[] value = onClickAnnotation.value();

                //获取EventType info
                EventType eventType = onClickAnnotation.annotationType().getAnnotation(EventType.class);
                Class listenerType = eventType.listenerType();
                String listenerSetter = eventType.listenerSetter();
                String methodName = eventType.methodName();

                for (int id : value ) {

                    ProxyHandler proxyHandler = new ProxyHandler(activity);
                    Object listener = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class[]{listenerType}, proxyHandler);
                    proxyHandler.mapMethod(methodName, method);
                    try {
                        Method findViewByIdMethod = activityClass.getMethod("findViewById", int.class);
                        findViewByIdMethod.setAccessible(true);
                        View targetView = (View) findViewByIdMethod.invoke(activity, id);
                        Method listenerSetMethod = targetView.getClass().getMethod(listenerSetter, listenerType);
                        listenerSetMethod.setAccessible(true);
                        //现在不知道具体是什么，通过设置代理到具体类中查找
                        listenerSetMethod.invoke(targetView, listener);

                    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }
            }

        }

    }

}
