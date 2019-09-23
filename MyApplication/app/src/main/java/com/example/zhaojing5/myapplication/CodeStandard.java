package com.example.zhaojing5.myapplication;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.zhaojing5.myapplication.Utils.ToastUtils;

/**
 * created by zhaojing 2019/5/15 上午10:33
 */
public class CodeStandard {


    public static void func1(@NonNull String param1, @NonNull String param2){
        ToastUtils.showToast(TestApplication.getInstance().getApplicationContext(),"代码规范 func1...");
    }




}
