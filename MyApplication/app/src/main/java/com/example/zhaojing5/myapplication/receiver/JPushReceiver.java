package com.example.zhaojing5.myapplication.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;

/**
 * created by zhaojing 2019/4/30 上午11:46
 */
public class JPushReceiver extends BroadcastReceiver {

    public static final String TAG = JPushReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.i(TAG,"action is " + intent.getAction());
        if(JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())){
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.i(TAG,"[JPushReceiver] 接收 registration id is " + regId);
        }else if(JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())){
            //自定义消息不会显示在通知栏，需要开发者代码处理
            Log.i(TAG,"收到了自定义消息，内容为：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        }else if(JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())){
            //在这里可以做一些统计或者做些其他工作
            Log.i(TAG,"收到了通知。");
        }else if(JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())){
            //在这里可以自己写代码去定义用户点击后的行为
            Log.i(TAG,"用户点击打开了通知");
        }else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())){


        }else{
            Log.i(TAG,"unhandled intent : " + intent.getAction());
        }
    }
}
