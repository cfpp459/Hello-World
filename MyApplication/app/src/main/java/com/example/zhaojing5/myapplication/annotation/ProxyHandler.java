package com.example.zhaojing5.myapplication.annotation;

import android.app.Activity;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

/**
 * created by zhaojing 2020/3/12 下午5:19
 */
public class ProxyHandler implements InvocationHandler {
    public static final String TAG = ProxyHandler.class.getSimpleName();
    private WeakReference<Activity> mActivity;
    private HashMap<String, Method> mMethodHashMap;


    public ProxyHandler(Activity activity){
        mActivity = new WeakReference<>(activity);
        mMethodHashMap = new HashMap<>();
    }

    public void mapMethod(String name, Method method){
        mMethodHashMap.put(name, method);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Log.i(TAG, "method name is " + method.getName() + ", args is " + Arrays.toString(args));
        Object object = mActivity.get();
        if (mActivity == null) {
            return null;
        }

        String name = method.getName();
        Method realMethod = mMethodHashMap.get(name);
        if (realMethod != null) {
            return realMethod.invoke(object, args);
        }

        return null;
    }

}
