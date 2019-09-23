package com.example.zhaojing5.myapplication.receiver;

import android.util.Log;

import cn.jpush.android.service.WakedResultReceiver;

/**
 * created by zhaojing 2019/4/30 下午4:47
 */
public class JPushWakedReceiver extends WakedResultReceiver {
    public static final String TAG = JPushWakedReceiver.class.getSimpleName();

    @Override
    public void onWake(int i) {
        super.onWake(i);
        Log.i(TAG,"被拉起回调，拉起方式是 " + i);
    }
}
